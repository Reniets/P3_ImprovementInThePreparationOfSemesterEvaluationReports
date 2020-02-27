package Interface.Controllers.MainContents;

import Interface.Components.QuickLongTooltip;
import Interface.Components.RawDataModuleCell;
import Interface.Controllers.LoadToggler;
import Interface.Views.MainContents.CommentPane;
import Interface.Views.MainContents.RawDataColumnFilter;
import dbComponent.Context.DataModelContext;
import dbComponent.Models.ModuleModel;
import dbComponent.Models.QuestionModel;
import dbComponent.Models.RawDataTable.Row;
import dbComponent.Models.RawDataTable.TableData;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RawdataController implements LoadToggler {

    @FXML
    private ComboBox<Pair<Integer, String>> cbxModule;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView tbvRawData;
    @FXML
    private HBox hbxColumns;
    @FXML
    private HBox hbxLoading;
    @FXML
    private AnchorPane acpControlBar;
    @FXML
    private StackPane stpContents;

    private List<TableColumn> columns;

    private CommentPane commentPane;
    private DataModelContext context = DataModelContext.getInstance();
    private TableData tableData;
    private ObservableList<Row> observableValue;
    private FilteredList<Row> filteredList;
    private boolean btnSearchClickedSinceChange;
    private Predicate<Row> currentTextPredicate = (r -> true);

    public RawdataController() {
        Platform.runLater(() -> this.tbvRawData.requestFocus());
    }

    void initColumnChoices() {
        RawDataColumnFilter filter = new RawDataColumnFilter(this.columns);

        this.hbxColumns.getChildren().add(filter);
    }

    private Predicate<Row> getModulePredicate() {
        if (cbxModule.getValue().getKey().equals(0)) {
            return (r -> true);
        } else {
            return (r -> r.getModuleID() == cbxModule.getValue().getKey());
        }
    }

    private Predicate<Row> getTextPredicate() {
        Pattern pattern = Pattern.compile(this.txtSearch.getText());

        if (!this.txtSearch.getText().isEmpty() && this.btnSearchClickedSinceChange) {
            this.currentTextPredicate = (row -> row.anyColumnMatchesPattern(pattern));
        } else if (this.txtSearch.getText().isEmpty() && this.btnSearchClickedSinceChange) {
            this.currentTextPredicate = (a -> true);
        }
        return this.currentTextPredicate;
    }

    public void updateFilter() {
        this.filteredList.setPredicate(getModulePredicate().and(getTextPredicate()));
        this.tbvRawData.requestFocus();
    }

    public void setCbxModuleContent(List<ModuleModel> modules) {
        List<Pair<Integer, String>> selectables = new ArrayList<>();

        for (ModuleModel module : modules) {
            Pair<Integer, String> selectable = new Pair<>(module.getId(), module.getName());
            selectables.add(selectable);
        }

        Pair<Integer, String> allModules = new Pair<>(0, "Alle Moduler");

        selectables.add(0, allModules);

        this.cbxModule.setItems(FXCollections.observableList(selectables));
        this.cbxModule.setValue(allModules);
        this.cbxModule.setConverter(new StringConverter<Pair<Integer, String>>() {
            @Override
            public String toString(Pair<Integer, String> integerStringPair) {
                return integerStringPair.getValue();
            }

            @Override
            public Pair<Integer, String> fromString(String s) {
                return cbxModule.getItems().stream().filter(p -> p.getValue().equalsIgnoreCase(s)).findFirst().orElse(null);
            }
        });
    }

    public void setupView() {

        this.setupCommentPane();

        this.columns = new ArrayList<>();

        tableData = context.getRawTableData();

        setCbxModuleContent(this.tableData.getModules());

        TableColumn<Row, String> module = new TableColumn<>("Modul");
        module.setCellFactory(rowStringTableColumn -> new RawDataModuleCell());


        Label moduleHeader = new Label("Modul");
        moduleHeader.setTooltip(new QuickLongTooltip("Modul"));

        module.setGraphic(moduleHeader);

        TableColumn<Row, String> respondant = new TableColumn<>("Respondant");
        respondant.setCellValueFactory(cd -> new SimpleStringProperty(((Integer) cd.getValue().getRespondantID()).toString()));

        Label respondantLabel = new Label("Respondant");
        respondantLabel.setTooltip(new QuickLongTooltip("Respondant"));

        respondant.setGraphic(respondantLabel);

        columns.add(module);
        columns.add(respondant);

        for (QuestionModel question : tableData.getQuestions()) {
            RawDataTableColumn column = new RawDataTableColumn(question, this.commentPane);
            columns.add(column);
        }

        tbvRawData.getColumns().setAll(this.columns);

        this.observableValue = FXCollections.observableList(this.tableData);

        context.addRawDataLoadedListener(this);
        context.loadRawDataInto(this.observableValue);

        this.filteredList = new FilteredList<>(this.observableValue, r -> true);

        tbvRawData.setItems(this.filteredList);

        this.txtSearch.setOnInputMethodTextChanged(inputMethodEvent -> this.btnSearchClickedSinceChange = false);
        this.txtSearch.setOnAction(actionEvent -> filterByText());

        this.btnSearch.setOnAction(actionEvent -> filterByText());

        this.initColumnChoices();
    }

    private void setupCommentPane() {
        this.commentPane = new CommentPane(() -> {
            this.acpControlBar.setDisable(false);
            this.acpControlBar.setVisible(true);
            this.commentPane.setVisible(false);
        }, () -> {
            this.acpControlBar.setDisable(true);
            this.acpControlBar.setVisible(false);
        });
        this.stpContents.getChildren().add(this.commentPane);
        this.commentPane.setVisible(false);
    }

    private void filterByText() {
        this.btnSearchClickedSinceChange = true;
        this.updateFilter();
        this.txtSearch.requestFocus();
        this.txtSearch.selectAll();
    }

    private void toggleLoadingData() {
        if (this.tbvRawData.isDisabled()) {
            this.tbvRawData.setDisable(false);
            this.acpControlBar.setDisable(false);
            this.hbxLoading.setVisible(false);
        } else {
            this.tbvRawData.setDisable(true);
            this.acpControlBar.setDisable(true);
            this.hbxLoading.setVisible(true);
        }
    }

    @Override
    public void toggleLoading() {
        this.toggleLoadingData();
    }
}
