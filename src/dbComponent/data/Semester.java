package dbComponent.data;

import dbComponent.Models.Named;
import dbComponent.enums.SemesterSeason;

import java.util.Objects;

public class Semester implements Named {

    // Fields:
    private int id;
    private int coordinatorID;
    private String name;
    private int year;
    private SemesterSeason season;

    // Constructors:
    //Added for the purpose of using the fields of this class in another model, without having to instantiate them in advance
    protected Semester() {

    }

    public Semester(int coordinatorID, String name, int year, SemesterSeason season) {
        this.coordinatorID = coordinatorID;
        this.name = name;
        this.year = year;
        this.season = season;
    }

    public Semester(int id, int coordinatorID, String name, int year, SemesterSeason season) {
        this.id = id;
        this.coordinatorID = coordinatorID;
        this.name = name;
        this.year = year;
        this.season = season;
    }

    // Setter:
    public void setId(int id) {
        this.id = id;
    }

    // Getters:
    public int getId() {
        return id;
    }

    public int getCoordinatorID() {
        return coordinatorID;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public SemesterSeason getSeason() {
        return season;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Semester semester = (Semester) o;
        return id == semester.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
