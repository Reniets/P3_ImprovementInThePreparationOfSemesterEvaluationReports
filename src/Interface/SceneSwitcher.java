package Interface;

import Interface.Controllers.LoginController;
import Interface.Controllers.SidebarController;
import Interface.Views.SemesterCreatePane;
import Interface.Views.SemesterPickPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class SceneSwitcher {

    // Fields:
    private Stage primaryStage;
    private SidebarController mainPageController;
    private boolean currentSceneIsMain;

    // Constants:
    private static final int MIN_HEIGHT = 650;
    private static final int MIN_WIDTH = 600;

    // constructor:
    public SceneSwitcher(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // MinHeight of has to be at least 650 for the sidebar to display properly.
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    private void showStage() {
        if (!this.primaryStage.isShowing()) {
            this.primaryStage.show();
        }
    }

    public void test() {
        SemesterCreatePane control = new SemesterCreatePane(this);

        this.primaryStage.setScene(new Scene(control));
        showStage();
    }

    public void showLoginScreen() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/Login.fxml"));
        Parent root = loader.load();
        LoginController contr = loader.getController();
        contr.setSwitcher(this);

        this.primaryStage.setScene(new Scene(root));
        this.currentSceneIsMain = false;
        showStage();
    }

    private void loadMainPage() throws Exception {
        if (!this.currentSceneIsMain) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/Sidebar.fxml"));
            Parent root = loader.load();
            this.mainPageController = loader.getController();

            this.mainPageController.setSwitcher(this);

            this.mainPageController.initialize();
            this.primaryStage.setScene(new Scene(root));
            this.currentSceneIsMain = true;
        }
    }

    public void showSemesterPickerScreen() throws Exception {
        loadMainPage();

        SemesterPickPane semesterPicker = new SemesterPickPane(this);

        semesterPicker.loadData();

        this.mainPageController.setContentAnchorPane(semesterPicker);

        showStage();
    }

    public void showSemesterCreateScreen() throws Exception {

        if (this.mainPageController == null) {
            loadMainPage();
        }

        this.mainPageController.setContentAnchorPane(new SemesterCreatePane(this));
        this.mainPageController.enableHomebox();
        showStage();
    }

    public void showSemesterPage() throws SQLException, ClassNotFoundException {
        this.mainPageController.semesterChosen();
    }
}
