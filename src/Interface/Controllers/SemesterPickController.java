package Interface.Controllers;

import Interface.Views.MainContents.GUISemester;
import Interface.Views.MainContents.SemesterYearSeason;
import dbComponent.Models.SemesterModel;
import dbComponent.data.Comparators.SemesterComparator;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class SemesterPickController extends ControllerBase {

    @FXML
    private ListView semesterYearRows;
    @FXML
    private AnchorPane anpLoading;
    @FXML
    private AnchorPane anpSemester;

    public SemesterPickController() {
        Platform.runLater(() -> this.semesterYearRows.requestFocus());
    }

    public void toggleLoading() {
        if (this.anpLoading.isVisible()) {
            this.anpLoading.setVisible(false);
            this.anpSemester.setDisable(false);
        } else {
            this.anpSemester.setDisable(true);
            this.anpLoading.setVisible(true);
        }
    }

    public void addSemesterRow(SemesterYearSeason semesterRow) {
        List<GUISemester> GUISemesters = semesterRow.getGUISemesters();

        for (GUISemester gUISemester : GUISemesters) {
            gUISemester.setOnMouseClicked((sem) -> {
                semesterChosen(gUISemester.getSemesterId());
            });
        }

        semesterYearRows.getItems().add(semesterRow);
    }

    public void semesterChosen(int id) {
        this.toggleLoading();
        this.context.setCurrentSemester(id, () -> {
            try {
                this.switcher.showSemesterPage();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        //this.switcher.showSemesterPage();
    }

    public void showSemesterCreate(Event mouseEvent) {
        try {
            this.switcher.showSemesterCreateScreen();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() {

        List<SemesterModel> semesters = new ArrayList<>(this.context.getSemesters());

        Collections.sort(semesters, new SemesterComparator());

        while (semesters.size() > 0) {
            //Places all semesters where the year and season is equal to the one in position 0 into semesterSeason. The next line removes these from the general list of semesters
            List<SemesterModel> semesterSeason = semesters.stream().filter(
                    semester -> (semester.getSeason().equals(semesters.get(0).getSeason()) &&
                            semester.getYear() == semesters.get(0).getYear())
            ).collect(Collectors.toList());
            semesters.removeAll(semesterSeason);

            SemesterYearSeason semesterRow = new SemesterYearSeason(semesterSeason.get(0).getYear(), semesterSeason.get(0).getSeason());
            for (SemesterModel semester : semesterSeason) {
                semesterRow.addSemester(semester);
            }
            this.addSemesterRow(semesterRow);
        }
    }
}
