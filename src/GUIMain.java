import Interface.Controllers.SemesterCreateController;
import Interface.Controllers.SemesterPickController;
import Interface.SceneSwitcher;
import Interface.Views.MainContents.SemesterYearSeason;
import dbComponent.enums.SemesterSeason;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUIMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        SceneSwitcher switcher = new SceneSwitcher(primaryStage);

        //switcher.showLoginScreen();
        switcher.showLoginScreen();
        primaryStage.setTitle("Semester Feedback Analysator");


    }


    private void doSemesterCreate(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Interface/Views/SemesterCreate.fxml"));
        Parent root = loader.load();
        SemesterCreateController contr = loader.getController();

        contr.setSemesterYears(10, 1);
        contr.setSemesterNumbers(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //contr.dynamicSemesterName();

        primaryStage.setTitle("Feedback Analyzer");

        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.show();
    }

    //Can be used to show the semesterpicker, though, it is loaded with static, nonsense data.
    //Also: this shows how to get scene and controller of the given scene.
    private void doSemesterPicker(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Interface/Views/SemesterPick.fxml"));
        Parent root = loader.load();
        SemesterPickController contr = loader.getController();

        int semesterId = 500;


        for (int i = 2018; i > 2011; i--) {
            SemesterYearSeason semRow2 = new SemesterYearSeason(i, SemesterSeason.AUTUMN);
            semRow2.addSemester(semesterId, "DAT3");
            semesterId--;
            semRow2.addSemester(semesterId, "SOFT3");
            semesterId--;
            semRow2.addSemester(semesterId, "IDX7");
            semesterId--;

            SemesterYearSeason semRow1 = new SemesterYearSeason(i, SemesterSeason.SPRING);
            semRow1.addSemester(semesterId, "DAT4");
            semesterId--;
            semRow1.addSemester(semesterId, "SOFT4");
            semesterId--;
            semRow1.addSemester(semesterId, "IDX6");
            semesterId--;

            contr.addSemesterRow(semRow2);
            contr.addSemesterRow(semRow1);

        }

        primaryStage.setTitle("Feedback AnalyzEr");

        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.show();
    }
}