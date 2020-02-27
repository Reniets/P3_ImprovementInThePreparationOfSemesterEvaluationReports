package Interface.Controllers;

import Interface.Views.MainContents.ComparisonPane;
import Interface.Views.MainContents.MarkingsPane;
import Interface.Views.MainContents.RawdataPane;
import Interface.Views.MainContents.SentimentPane;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.sql.SQLException;

public class SidebarController extends ControllerBase {

    @FXML
    private AnchorPane tabMarkings;
    @FXML
    private AnchorPane tabRawData;
    @FXML
    private AnchorPane homeBox;
    @FXML
    private Label semesterLabel;
    @FXML
    private Line homeBoxDividerLine;
    @FXML
    private TabPane analyseTabPane;
    @FXML
    private AnchorPane contentAnchorPane;
    @FXML
    private Label name;
    @FXML
    private Button tab1Button;
    @FXML
    private Button tab2Button;
    @FXML
    private Button tab3Button;
    @FXML
    private Button tab4Button;

    private MarkingsPane markings;
    private RawdataPane rawData;
    private Button selectedTabButton;


    // initialize the necessary fields
    public void initialize() {
        this.name.setText(this.context.getCoordinator().getEmail());
        //this.title.setText(title);
    }

    ////////////
    // INPUTS //
    ////////////

    // Action of the logoutButton
    @FXML
    private void logoutButtonAction() {
        try {
            this.context.logoutCoordinator();
            switcher.showLoginScreen();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // MouseClicked Method for the homeBox Pane
    @FXML
    private void homeBoxClicked() {
        analysisTabpaneEnable(false);
        enableNode(semesterLabel, false);
        semesterLabel.setText("");
        disableHomebox();
    }

    /////////////
    // SIDEBAR //
    /////////////

    // Public method for enabling the homeBox
    public void enableHomebox() {
        homeBox.setVisible(true);
        homeBox.setDisable(false);
        homeBoxDividerLine.setVisible(true);
    }

    // Public method for disabling the homebox
    public void disableHomebox() {
        homeBox.setVisible(false);
        homeBox.setDisable(true);
        homeBoxDividerLine.setVisible(false);
        try {
            this.switcher.showSemesterPickerScreen();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // Method for when a semester is chosen
    public void semesterChosen() throws SQLException, ClassNotFoundException {

        enableHomebox();
        enableNode(semesterLabel, true);
        semesterLabel.setText(this.context.getCurrentSemester().getName());

        contentAnchorPane.getChildren().clear();

        analysisTabpaneEnable(true);
        setAnalyseTabPane_tab1(new SentimentPane());
        setAnalyseTabPane_tab2(new ComparisonPane());

        this.setRawDataTab();
        this.setMarkingTab();


        tabSelectionToggle(tab1Button);
    }

    public void tab1ButtonAction() {
        analyseTabPane.getSelectionModel().select(0);
        tabSelectionToggle(tab1Button);
    }

    public void tab2ButtonAction() {
        analyseTabPane.getSelectionModel().select(1);
        tabSelectionToggle(tab2Button);
    }

    public void tab3ButtonAction() {
        analyseTabPane.getSelectionModel().select(2);
        tabSelectionToggle(tab3Button);
    }

    public void tab4ButtonAction() {
        analyseTabPane.getSelectionModel().select(3);
        tabSelectionToggle(tab4Button);
    }

    //////////////
    // CONTENTS //
    //////////////

    // Set the content to be displayed before the TabPane becomes available
    public void setContentAnchorPane(Node content) {

        contentAnchorPane.getChildren().clear();
        contentAnchorPane.getChildren().add(content);

        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

    // Fill in Tab1
    private void setAnalyseTabPane_tab1(Node content) {
        analyseTabPane.getTabs().get(0).setContent(content);
        anchorsAllZero(content);
    }

    // Fill in Tab2
    private void setAnalyseTabPane_tab2(Node content) {
        analyseTabPane.getTabs().get(1).setContent(content);
        anchorsAllZero(content);
    }

    //Fill in Tab3
    public void setMarkingTab() {
        if (this.tabMarkings.getChildren().size() == 0) {
            this.markings = new MarkingsPane();

            this.anchorsAllZero(this.markings);

            this.tabMarkings.getChildren().add(this.markings);
        }
    }

    // Fill in Tab4
    public void setRawDataTab() {
        if (this.tabRawData.getChildren().size() == 0) {
            this.rawData = new RawdataPane();

            this.tabRawData.getChildren().add(this.rawData);

            anchorsAllZero(this.rawData);
        }
    }


    /////////////////////
    // Private methods //
    /////////////////////

    // Set all the anchors of a Node to 0
    private void anchorsAllZero(Node content) {
        AnchorPane.setRightAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setTopAnchor(content, 0.0);
    }

    // Enable/disable the analysisTabPane and everything associated with it
    private void analysisTabpaneEnable(Boolean b) {
        enableNode(analyseTabPane, b);
        enableNode(tab1Button, b);
        enableNode(tab2Button, b);
        enableNode(tab3Button, b);
        enableNode(tab4Button, b);
    }

    // Enable/disable a Node
    private void enableNode(Node n, boolean b) {
        n.setDisable(!b);
        n.setVisible(b);
    }

    // Style a button as selected, and remove that style from the previous holder.
    private void tabSelectionToggle(Button selectButton) {
        // If there is nothing selected, select the given button
        // else move the selection from the previous button to the given button
        if (selectedTabButton == null) {
            selectedTabButton = selectButton;
            selectedTabButton.getStyleClass().add("selected");
        } else {
            selectedTabButton.getStyleClass().remove(selectedTabButton.getStyleClass().indexOf("selected"));
            selectButton.getStyleClass().add("selected");
            selectedTabButton = selectButton;
        }
    }
}
