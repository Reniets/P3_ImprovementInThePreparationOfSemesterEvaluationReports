package Interface.Controllers.MainContents;

import Enums.SentimentPolarity;
import Exceptions.NotInPaneException;
import Interface.Components.DefaultColors;
import Interface.Components.NamedObjectCellFactory;
import Interface.Components.QuickLongTooltip;
import Interface.Components.SelfdestructButton;
import Interface.Controllers.ControllerBase;
import dbComponent.Context.DataModelContext;
import dbComponent.Models.ModuleModel;
import dbComponent.Models.SemesterModel;
import dbComponent.Models.SentimentModel;
import dbComponent.database.DB;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComparisonController extends ControllerBase {

    @FXML
    private Button openPickComparisonBoxButton;
    @FXML
    private Button compareButton;
    @FXML
    private AnchorPane compareWithBox;
    @FXML
    private Label compare_SemesterLabel;
    @FXML
    private VBox semesterLegend;
    @FXML
    private ComboBox<ModuleModel> compare_PickMainModuleCombo;

    // pickComparisonBox
    @FXML
    private AnchorPane pickComparisonBox;
    @FXML
    private ComboBox<SemesterModel> comparisonPickSemesterCombo;
    @FXML
    private ComboBox<ModuleModel> comparisonPickModuleCombo;
    @FXML
    private Button addComparisonButton;
    @FXML
    private Button pickComparisonXButton;

    // Graphbox
    @FXML
    private AnchorPane graphBox;
    @FXML
    private BarChart sentimentCompareBarChart;

    private DataModelContext context = DataModelContext.getInstance();
    private ArrayList<ModuleModel> chosenModuleList;
    private DefaultColors defaultColors;

    //
    public void initialize() {
        // Initialize stuff
        this.defaultColors = new DefaultColors();
        this.chosenModuleList = new ArrayList<>();

        // Set combobox cellFactories
        NamedObjectCellFactory<SemesterModel> semesterCellFactory = new NamedObjectCellFactory<>();
        NamedObjectCellFactory<ModuleModel> moduleCellFactory = new NamedObjectCellFactory<>();
        comparisonPickSemesterCombo.setCellFactory(semesterCellFactory);
        comparisonPickModuleCombo.setCellFactory(moduleCellFactory);
        compare_PickMainModuleCombo.setCellFactory(moduleCellFactory);
        // Update the ButtonCell of the Comboboxes to match the factory
        comparisonPickSemesterCombo.setButtonCell(semesterCellFactory.call(null));
        comparisonPickModuleCombo.setButtonCell(moduleCellFactory.call(null));
        compare_PickMainModuleCombo.setButtonCell(moduleCellFactory.call(null));

        // Fix the BarChart
        //XYChart.Series<String, Integer> series = new XYChart.Series<>();
        //series.getData().addAll(new XYChart.Data<String, Integer>("Positiv", 2), new XYChart.Data<String, Integer>("Negativ", 3));
        //sentimentCompareBarChart.getData().add(series);

        // Set contents
        this.compare_SemesterLabel.setText(context.getCurrentSemester().getDisplayName());
        compare_PickMainModuleCombo.setItems(FXCollections.observableList(context.getCurrentSemester().getModules()));
        compare_PickMainModuleCombo.setValue(compare_PickMainModuleCombo.getItems().get(0));
        comparisonPickSemesterCombo.setItems(new FilteredList<SemesterModel>(FXCollections.observableList(context.getSemesters())));
        comparisonPickSemesterCombo.setValue(comparisonPickSemesterCombo.getItems().get(0));
        pickedSemester();
    }


    ////////////
    // Inputs //
    ////////////

    // Method of comparisonPickSemesterButton
    public void addModule() throws SQLException, ClassNotFoundException {
        // Fetch the chosen module, and add it to the list
        ModuleModel module = comparisonPickModuleCombo.getValue();
        chosenModuleList.add(module);

        // Create a box to display the selection in
        AnchorPane selectionDisplayBox = new AnchorPane();
        Label selectionDisplayLabel = new Label(module.getSemester().getDisplayName() + System.lineSeparator() + module.getDisplayName());
        SelfdestructButton selectionDisplaySelfdestructButton;

        // Anchor it to the Pane
        AnchorPane.setTopAnchor(selectionDisplayBox, 0.0);
        AnchorPane.setLeftAnchor(selectionDisplayBox, 0.0);
        AnchorPane.setRightAnchor(selectionDisplayBox, 0.0);
        selectionDisplayBox.setPrefHeight(50);
        compareWithBox.getChildren().add(selectionDisplayBox);

        // A Method object for the extra functionality of the SelfdestructButton
        Method buttonfunction;
        try {
            buttonfunction = this.getClass().getMethod("updateCompareWithBox", null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // SelfdestructButton linked to the chosen Module, its displaybox, and the chosenModuleList
        try {
            selectionDisplaySelfdestructButton = new SelfdestructButton("X", module, chosenModuleList, selectionDisplayBox, buttonfunction, this);
        } catch (NotInPaneException e) {
            throw new RuntimeException(e.getMessage());
        }

        // Anchor the contents of the displaybox
        AnchorPane.setTopAnchor(selectionDisplayLabel, 5.0);
        AnchorPane.setBottomAnchor(selectionDisplayLabel, 10.0);
        AnchorPane.setLeftAnchor(selectionDisplayLabel, 10.0);
        AnchorPane.setTopAnchor(selectionDisplaySelfdestructButton, 10.0);
        AnchorPane.setBottomAnchor(selectionDisplaySelfdestructButton, 10.0);
        AnchorPane.setRightAnchor(selectionDisplaySelfdestructButton, 10.0);

        // Add the contents to the displaybox
        selectionDisplayBox.getChildren().add(selectionDisplayLabel);
        selectionDisplayBox.getChildren().add(selectionDisplaySelfdestructButton);

        // Border for the displaybox
        selectionDisplayBox.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: BLACK");

        // Tooltip for full name
        QuickLongTooltip selectionDisplayTooltip = new QuickLongTooltip(module.getSemester().getName() + System.lineSeparator() + module.getName());
        Tooltip.install(selectionDisplayBox, selectionDisplayTooltip);

        // Display the children of the displayboxbox in order under each other
        updateCompareWithBox();

        // Close the picking box
        closePickComparisonBox();
    }

    // Method of the Pick Semester ComboBox
    public void pickedSemester() {
        // Comparator that prioritizes models with names matching the
        comparisonPickModuleCombo.setItems(FXCollections.observableList(comparisonPickSemesterCombo.getValue().getModules()));
        comparisonPickModuleCombo.getItems().sort(moduleComparator());
        comparisonPickModuleCombo.setValue(comparisonPickModuleCombo.getItems().get(0));
    }

    // Method of the main Module pick ComboBox
    public void compare_PickMainModuleComboAction() {
        // Update the module sorting
        comparisonPickModuleCombo.getItems().sort(moduleComparator());
        // if the picked module is also available in the add module box, default to picking that option.
        if (comparisonPickModuleCombo.getItems().get(0).getName().equals(compare_PickMainModuleCombo.getValue().getName())) {
            comparisonPickModuleCombo.setValue(compare_PickMainModuleCombo.getValue());
        }
    }

    // Method of compareButton
    // Fill the graph according to the chosen points of comparison
    public void search() throws SQLException, ClassNotFoundException {
        // Make graph visible
        graphBox.setVisible(true);
        graphBox.setDisable(false);
        // Remove everything besides the graph box and the legend
        sentimentCompareBarChart.getData().clear();
        semesterLegend.getChildren().clear();
        sentimentCompare();
    }

    // Open the box for picking modules to compare with
    public void openPickComparisonBox() {
        pickComparisonBox.setDisable(false);
        pickComparisonBox.setVisible(true);
        openPickComparisonBoxButton.setDisable(true);
        compareButton.setDisable(true);
    }

    // Close the box for picking modules to compare with
    public void closePickComparisonBox() {
        pickComparisonBox.setDisable(true);
        pickComparisonBox.setVisible(false);
        openPickComparisonBoxButton.setDisable(false);
        compareButton.setDisable(false);
    }

    ///////////////////////
    // Private functions //
    ///////////////////////

    // Fill the "Sentiment" tab of comparisons
    private void sentimentCompare() throws SQLException, ClassNotFoundException {
        // Dataseries for the BarChart to display
        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Shithole");

        // Add modules to legend with color pickers
        sentimentCompareBarChart.getData().add(addDataSeriesEntry(compare_PickMainModuleCombo.getValue(), 0));
        int i = 1;
        for (ModuleModel module : this.chosenModuleList) {
            // Max 8 elements
            if (i > 7) {
                break;
            }
            sentimentCompareBarChart.getData().add(addDataSeriesEntry(module, i));
            i++;
        }

        // Apply colors to the bars
        for (int colorIndex = 0; colorIndex < 8; colorIndex++) {
            Color color = defaultColors.getDefaultColorByIndex(colorIndex);
            changeBarChartColor(color.getRed(), color.getGreen(), color.getBlue(), colorIndex);
        }
    }

    private XYChart.Series<String, Integer> addDataSeriesEntry(ModuleModel module, int colorIndex) throws SQLException, ClassNotFoundException {
        Label label = new Label(module.getName());
        ColorPicker colorPicker = new ColorPicker(defaultColors.getDefaultColorByIndex(colorIndex));
        colorPicker.setOnAction(colorpickerEvent(colorPicker, colorIndex));
        Button button = new Button("Se Sentimentanalyse");
        HBox hBox = new HBox(colorPicker, button);
        Region spacing = new Region();
        spacing.setPrefHeight(10);
        this.semesterLegend.getChildren().addAll(label, hBox);
        this.semesterLegend.getChildren().add(spacing);
        DB database = new DB();

        List<SentimentModel> sentiments = database.SELECT_SentimentsWhereModuleID(module.getId());
        int negativeScore = 0;
        int positiveScore = 0;

        for (SentimentModel sentiment : sentiments) {
            if (sentiment.getSentimentPolarity() == SentimentPolarity.NEGATIVE) {
                negativeScore++;
            } else if (sentiment.getSentimentPolarity() == SentimentPolarity.POSITIVE) {
                positiveScore++;
            } else {
                throw new RuntimeException("Null sentiment received from database");
            }
        }

        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
        dataSeries.getData().add(new XYChart.Data<String, Integer>("Positiv", positiveScore));
        dataSeries.getData().add(new XYChart.Data<String, Integer>("Negativ", negativeScore));

        return dataSeries;
    }

    // Display the children of the displayboxbox in order under each other
    // Must be public for external access through Method object, despite what Intellij thinks
    @SuppressWarnings("WeakerAccess")
    public void updateCompareWithBox() {
        double d = 0;
        for (Node child : compareWithBox.getChildren()) {
            if (d > 7) {
                break;
            }

            AnchorPane.setTopAnchor(child, d * 50);
            d++;
        }
    }

    private EventHandler<ActionEvent> colorpickerEvent(ColorPicker colorPicker, int colorIndex) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeBarChartColor(colorPicker.getValue().getRed(), colorPicker.getValue().getGreen(), colorPicker.getValue().getBlue(), colorIndex);
            }
        };
    }

    private void changeBarChartColor(double red, double green, double blue, int colorIndex) {
        int r = (int) Math.round(red * 255.0);
        int g = (int) Math.round(green * 255.0);
        int b = (int) Math.round(blue * 255.0);

        for (Node n : this.sentimentCompareBarChart.lookupAll(".default-color" + colorIndex + ".chart-bar")) {
            n.setStyle("-fx-bar-fill: #" + String.format("%02X", r) + String.format("%02X", g) + String.format("%02X", b));
        }
    }

    // Produces a comparator for the current state of the main module ComboBox
    private Comparator<ModuleModel> moduleComparator() {
        return new Comparator<ModuleModel>() {
            @Override
            public int compare(ModuleModel o1, ModuleModel o2) {
                // conventional return of 0 when equal
                if (o1.equals(o2)) {
                    return 0;
                }

                boolean m1 = o1.getName().equals(compare_PickMainModuleCombo.getValue().getName());
                boolean m2 = o2.getName().equals(compare_PickMainModuleCombo.getValue().getName());

                // if m1 matches and m2 doesn't
                if (m1 && !m2) {
                    return -1;
                } // if m2 matches and m1 doesn't
                else if (!m1 && m2) {
                    return 1;
                } // else sort alphabetially
                else {
                    return o1.getName().compareTo(o2.getName());
                }
            }
        };
    }

}