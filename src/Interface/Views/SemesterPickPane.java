package Interface.Views;

import Interface.Controllers.SemesterPickController;
import Interface.SceneSwitcher;
import Interface.Views.MainContents.SemesterYearSeason;
import dbComponent.enums.SemesterSeason;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class SemesterPickPane extends AnchorPane {

    // Field:
    private SemesterPickController controller;

    // Constructor:
    public SemesterPickPane(SceneSwitcher switcher) {
        this.controller = new SemesterPickController();
        this.controller.setSwitcher(switcher);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SemesterPick.fxml"));
        loader.setRoot(this);
        loader.setController(this.controller);

        try {
            loader.load();
            this.controller.toggleLoading();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // Methods:
    public void insertTestData() {
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

            this.controller.addSemesterRow(semRow2);
            this.controller.addSemesterRow(semRow1);

        }
    }

    public void loadData() {
        Task<Void> loader = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                controller.loadData();
                return null;
            }
        };

        loader.run();
    }
}
