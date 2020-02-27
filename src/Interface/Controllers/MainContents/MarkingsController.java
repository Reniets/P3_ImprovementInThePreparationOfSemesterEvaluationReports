package Interface.Controllers.MainContents;

import Interface.Components.HighlightColor;
import Interface.Components.HighlightComboBox;
import Interface.Components.MarkingsPaneComponents.*;
import Interface.Controllers.LoadToggler;
import Interface.Views.MainContents.CommentPane;
import dbComponent.Context.DataModelContext;
import dbComponent.Models.AnswerModel;
import dbComponent.Models.MarkedAnswerModel;
import dbComponent.Models.ModuleModel;
import dbComponent.Models.QuestionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class MarkingsController implements LoadToggler {

    @FXML
    private AnchorPane anpMarkingPane;
    @FXML
    private ComboBox<ModuleModel> cbxModule;
    @FXML
    private ComboBox<QuestionModel> cbxQuestion;

    private HighlightComboBox cbxColor;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<MarkedAnswerModel> tbvMarkings;

    @FXML
    private AnchorPane anpLoadingPane;
    @FXML
    private StackPane stpContents;

    private CommentPane commentPane;

    private DataModelContext context = DataModelContext.getInstance();
    private List<AnswerModel> answers = new ArrayList<>();
    private ObservableList<MarkedAnswerModel> observableList;// = FXCollections.observableList(this.answers);
    private FilteredList<MarkedAnswerModel> filteredList = new FilteredList<>(this.context.getCurrentSemester().getMarkedAnswerModels());

    private boolean btnSearchPushedSinceChange = false;
    private Predicate<MarkedAnswerModel> txtPredicate = (a -> true);

    private ModuleModel allModules = new ModuleModel(-1, -1, "Alle Moduler");
    private QuestionModel allQuestions = new QuestionModel(-1, -1, "Alle Spørgsmål");


    public void setupView() {
        this.setupCommentPane();
        this.context.addRawDataLoadedListener(this);
        this.setupTableColumns();
        this.initColorFilter();
        this.setupFilters();
    }

    private void initColorFilter() {
        this.cbxColor = new HighlightComboBox();
        this.anpMarkingPane.getChildren().add(this.cbxColor);
        this.anpMarkingPane.setTopAnchor(this.cbxColor, 18.0);
        this.anpMarkingPane.setLeftAnchor(this.cbxColor, 330.0);
    }

    private void setupTableColumns() {
        List<TableColumn<MarkedAnswerModel, String>> columns = new ArrayList<>();
        MarkingTableColumn markingColumn = new MarkingTableColumn();

        columns.add(new ModuleTableColumn());
        columns.add(new RespondantTableColumn());
        columns.add(new QuestionTableColumn());
        columns.add(new AnswerTableColumn(this.commentPane, markingColumn));
        columns.add(markingColumn);

        this.tbvMarkings.getColumns().setAll(columns);
        this.tbvMarkings.setItems(this.filteredList);
    }

    private void setupFilters() {
        ObservableList<ModuleModel> modules = FXCollections.observableList(this.context.getCurrentSemester().getModules());
        modules.add(0, this.allModules);

        ObservableList<QuestionModel> questions = FXCollections.observableList(this.context.getCurrentSemester().getQuestions());
        questions.add(0, this.allQuestions);

        this.cbxModule.setConverter(new StringConverter<ModuleModel>() {
            @Override
            public String toString(ModuleModel moduleModel) {
                return moduleModel == null ? "" : moduleModel.getName();
            }

            @Override
            public ModuleModel fromString(String s) {
                return null;
            }
        });
        this.cbxModule.setItems(FXCollections.observableList(this.context.getCurrentSemester().getModules()));
        this.cbxModule.setValue(this.allModules);
        this.cbxModule.setOnAction(actionEvent -> updateFilter());

        this.cbxQuestion.setConverter(new StringConverter<QuestionModel>() {
            @Override
            public String toString(QuestionModel questionModel) {
                return questionModel == null ? "" : questionModel.getQuestion();
            }

            @Override
            public QuestionModel fromString(String s) {
                return null;
            }
        });

        this.cbxQuestion.setOnAction(actionEvent -> updateFilter());

        this.cbxQuestion.setItems(FXCollections.observableList(this.context.getCurrentSemester().getQuestions()));
        this.cbxQuestion.setValue(this.allQuestions);

        this.cbxColor.setOnAction(actionEvent -> updateFilter());

        this.txtSearch.setOnInputMethodTextChanged(inputMethodEvent -> this.btnSearchPushedSinceChange = false);
        this.txtSearch.setOnAction(actionEvent -> this.filterByTxt());

        this.btnSearch.setOnAction(actionEvent -> filterByTxt());

    }

    private void filterByTxt() {
        this.btnSearchPushedSinceChange = true;
        this.updateFilter();
        this.txtSearch.requestFocus();
        this.txtSearch.selectAll();
    }

    private Predicate<MarkedAnswerModel> getColorFilter() {
        if (!this.cbxColor.getValue().equals(HighlightColor.NONE)) {
            return (a -> this.cbxColor.getValue().equals(new HighlightColor(a.getColour())));
        }
        return (a -> true);
    }

    private Predicate<MarkedAnswerModel> getModuleFilter() {
        if (!this.cbxModule.getValue().equals(this.allModules)) {
            return (a -> a.getAnswer().getModule().equals(this.cbxModule.getValue()));
        } else {
            return (a -> true);
        }
    }

    private Predicate<MarkedAnswerModel> getQuestionFilter() {
        if (!this.cbxQuestion.getValue().equals(this.allQuestions)) {
            return (a -> a.getAnswer().getQuestion().equals(this.cbxQuestion.getValue()));
        } else {
            return (a -> true);
        }
    }

    private Predicate<MarkedAnswerModel> getTextFilter() {
        Pattern pattern = Pattern.compile(this.txtSearch.getText());

        if (!this.txtSearch.getText().isEmpty() && btnSearchPushedSinceChange) {
            this.txtPredicate = (a -> pattern.matcher(a.getAnswer().getAnswer()).find());
        } else if (this.txtSearch.getText().isEmpty() && this.btnSearchPushedSinceChange) {
            this.txtPredicate = (a -> true);
        }
        return this.txtPredicate;
    }


    public void updateFilter() {
        Predicate<MarkedAnswerModel> predicate = this.getModuleFilter();
        predicate = predicate.and(this.getQuestionFilter());
        predicate = predicate.and(this.getColorFilter());
        predicate = predicate.and(this.getTextFilter());

        this.filteredList.setPredicate(predicate);
    }

    @Override
    public void toggleLoading() {
        if (this.anpMarkingPane.isDisabled()) {
            this.anpLoadingPane.setVisible(false);
            this.anpMarkingPane.setDisable(false);
        } else {
            this.anpMarkingPane.setDisable(true);
            this.anpLoadingPane.setVisible(true);
        }
    }

    private void setupCommentPane() {
        this.commentPane = new CommentPane(() -> {
            this.commentPane.setVisible(false);
            this.anpMarkingPane.setVisible(true);
            this.anpMarkingPane.setDisable(false);
            //this.reloadMarking(this.commentPane.getAnswer());

        }, () -> {
            this.anpMarkingPane.setDisable(true);
            this.anpMarkingPane.setVisible(false);
        });
        this.stpContents.getChildren().add(this.commentPane);
        this.commentPane.setVisible(false);
    }

    private void reloadMarking(AnswerModel answer) {
        MarkedAnswerModel markedAnswer = answer.getMarkedAnswer();
        if (markedAnswer != null) {
            int index = this.context.getCurrentSemester().getMarkedAnswerModels().indexOf(markedAnswer);
            this.context.getCurrentSemester().getMarkedAnswerModels().remove(index);
            this.context.getCurrentSemester().addMarkedAnswer(markedAnswer);
        }


    }
}
