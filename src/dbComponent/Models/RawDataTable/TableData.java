package dbComponent.Models.RawDataTable;

import dbComponent.Models.AnswerModel;
import dbComponent.Models.ModuleModel;
import dbComponent.Models.QuestionModel;
import dbComponent.Models.SemesterModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TableData extends ArrayList<Row> {

    // Fields:
    private SemesterModel semester;
    private List<QuestionModel> questions;
    private List<ModuleModel> modules;

    // Constructor:
    public TableData(SemesterModel semester) {
        this.semester = semester;

        this.questions = semester.getQuestions().stream().filter(
                question -> !(
                        question.getQuestion().equalsIgnoreCase("modul") ||
                                question.getQuestion().equalsIgnoreCase("respondentid"))
        ).collect(Collectors.toList());

        this.modules = semester.getModules();
    }

    // Methods:

    public List<QuestionModel> getQuestions() {
        return this.questions;
    }

    public List<ModuleModel> getModules() {
        return this.modules;
    }

    public SemesterModel getSemester() {
        return this.semester;
    }

    public void makeRows() {
        Set<Integer> respondentIDs = this.semester.getAnswers().stream().map(answerModel -> answerModel.getRespondentID()).collect(Collectors.toSet());

        for (ModuleModel module : this.semester.getModules()) {
            for (int respondant : respondentIDs) {
                List<AnswerModel> answers = new ArrayList<>();

                for (QuestionModel question : this.questions) {
                    Optional<AnswerModel> oAnswer = question.getAnswers().stream().filter(a -> a.getRespondentID() == respondant).findFirst();
                    AnswerModel answer = oAnswer.isPresent() ? oAnswer.get() : null;
                    answers.add(answer);
                }

                Row rawDataRow = new Row(module.getId(), respondant, answers);
                this.add(rawDataRow);
            }
        }
    }
}
