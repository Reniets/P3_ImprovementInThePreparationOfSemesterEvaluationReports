package Interface.Views.MainContents;

import dbComponent.data.Semester;
import dbComponent.enums.SemesterSeason;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class SemesterYearSeason extends HBox {

    // Fields:
    private int year;
    private SemesterSeason season;
    private List<GUISemester> GUISemesters;

    // Constant:
    private static final String LABEL_FORMAT = "%d-%s";

    // Constructors:
    public SemesterYearSeason(Semester semester) {
        this(semester.getYear(), semester.getSeason());
    }

    public SemesterYearSeason(int year, SemesterSeason season) {
        super(5);
        this.year = year;
        this.season = season;
        this.GUISemesters = new ArrayList<>();
    }

    // Methods:
    public List<GUISemester> getGUISemesters() {
        return this.GUISemesters;
    }

    public int getYear() {
        return this.year;
    }

    public SemesterSeason getSeason() {
        return this.season;
    }

    public void addSemester(int id, String name) {
        Label yearAndSeason = new Label(String.format(LABEL_FORMAT, this.year, this.season.toString()));
        GUISemester GUISemester = new GUISemester(id, name, yearAndSeason);
        this.GUISemesters.add(GUISemester);
        this.getChildren().add(GUISemester);
    }

    public void addSemester(Semester semester) {
        this.addSemester(semester.getId(), semester.getName());
    }
}
