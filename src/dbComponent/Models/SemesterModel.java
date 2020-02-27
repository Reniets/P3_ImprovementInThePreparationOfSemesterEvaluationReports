package dbComponent.Models;

import Interface.Components.GraphicUpdateListener;
import dbComponent.Context.DataModelContext;
import dbComponent.data.Semester;
import dbComponent.database.DB;
import dbComponent.enums.SemesterSeason;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SemesterModel extends Semester implements ModelInsert {
    // Fields:
    private static final String INSERT_STATEMENT = "INSERT INTO Semesters(CoordinatorID, Name, Year, Season) VALUES(?, ?, ?, ?)";

    private List<ModuleModel> modules;
    private List<QuestionModel> questions;
    private ObservableList<AnswerModel> answerModelObservableList = FXCollections.observableList(new ArrayList<>());
    private ObservableList<MarkedAnswerModel> markedAnswerModels = FXCollections.observableList(new ArrayList<>());
    private GraphicUpdateListener insertListener;

    // Constant:
    private static final int MAX_DISPLAY_NAME_LENGTH = 28;

    // Constructor:
    public SemesterModel(int semesterID, int coordinatorID, String name, int year, SemesterSeason season) {
        super(semesterID, coordinatorID, name, year, season);
    }

    public SemesterModel(int semesterID, int coordinatorID, String name, int year, String season) {
        super(semesterID, coordinatorID, name, year, SemesterSeason.fromString(season));
    }

    // Methods
    public List<ModuleModel> getModules() {
        if (modules == null) {
            try {
                this.modules = DataModelContext.getInstance().getSyncronousDatabase().SELECT_ModulesWhereSemesterId(this.getId());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return modules;
    }

    public List<QuestionModel> getQuestions() {
        return questions;
    }

    public ObservableList<AnswerModel> getAnswers() {
        return this.answerModelObservableList;
    }

    public ObservableList<MarkedAnswerModel> getMarkedAnswerModels() {
        return markedAnswerModels;
    }

    public String getDisplayName() {
        if (this.getName().length() > MAX_DISPLAY_NAME_LENGTH) {
            return this.getName().substring(0, MAX_DISPLAY_NAME_LENGTH);
        } else {
            return this.getName();
        }
    }

    public void attachAnswer(AnswerModel answer) {
        List<AnswerModel> answers = new ArrayList<>();
        answers.add(answer);
        this.attachAnswers(answers);
    }

    private void attachAnswers(List<AnswerModel> answers) {
        //this.answerModelObservableList.addAll(answers);

        for (QuestionModel question : this.questions) {
            List<AnswerModel> relatedAnswers = answers.stream().filter(answerModel -> answerModel.getQuestionID() == question.getId()).collect(Collectors.toList());
            question.setAnswers(relatedAnswers);
        }

        for (ModuleModel module : modules) {
            List<AnswerModel> relatedAnswers = answers.stream().filter(answerModel -> answerModel.getModuleID() == module.getId()).collect(Collectors.toList());
            module.setAnswers(relatedAnswers);
        }
    }

    public void addMarkedAnswer(MarkedAnswerModel markedAnswerModel) {
        if (!this.markedAnswerModels.contains(markedAnswerModel)) {
            this.markedAnswerModels.add(markedAnswerModel);
        }
    }

    public void attachMarkedAnswer(MarkedAnswerModel markedAnswerModel) {
        List<MarkedAnswerModel> marked = new ArrayList<>();
        marked.add(markedAnswerModel);
        attachMarkedAnswers(marked);
    }

    private void attachMarkedAnswers(List<MarkedAnswerModel> markedAnswerModels) {
        //Platform.runLater(() -> this.markedAnswerModels.addAll(markedAnswerModels));

        for (MarkedAnswerModel markedAnswer : markedAnswerModels) {
            Predicate<ModuleModel> modulePredicate = (module -> module.getId() == markedAnswer.getModuleID());
            Predicate<AnswerModel> answerPredicate = (answerModel ->
                    answerModel.getRespondentID() == markedAnswer.getRespondantID() &&
                            answerModel.getQuestionID() == markedAnswer.getQuestionID());

            AnswerModel answer = this.modules.stream().filter(modulePredicate).map(m -> m.getAnswers()).flatMap(Collection::stream).filter(answerPredicate).findFirst().orElse(null);
            if (answer != null) {
                answer.setMarkedAnswer(markedAnswer);
            }
        }
    }

    public void loadRelatedData() {
        try {
            this.modules = DataModelContext.getInstance().getSyncronousDatabase().SELECT_ModulesWhereSemesterId(this.getId());
            this.modules.forEach(module -> module.setSemester(this));
            this.questions = DataModelContext.getInstance().getSyncronousDatabase().SELECT_QuestionsWhereSemesterId(this.getId());
            this.questions.forEach(question -> question.setSemester(this));

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void InsertModel(DB db) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement(INSERT_STATEMENT, Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, this.getCoordinatorID());
        statement.setString(2, this.getName());
        statement.setInt(3, this.getYear());
        statement.setString(4, this.getSeason().toString());

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("Semester insert failed, no row update");
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            this.setId(generatedKeys.getInt(1));
        } else {
            throw new SQLException("Semester insert failed, no ID");
        }
        Platform.runLater(() -> this.insertListener.call());
    }

    @Override
    public void addToContext() {
        DataModelContext.getInstance().addToInsert(this);
    }

    @Override
    public void addInsertListener(Node source, GraphicUpdateListener listener) {
        this.insertListener = listener;
    }
}
