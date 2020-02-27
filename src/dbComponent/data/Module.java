package dbComponent.data;

import dbComponent.Models.Named;

import java.util.Objects;

public class Module implements Named {

    // Fields:
    private int id;
    private int semesterID;
    private String name;

    // Constructors:
    //Added for the purpose of using the fields of this class in another model, without having to instantiate them in advance
    protected Module() {

    }

    public Module(int semesterID, String name) {
        this.semesterID = semesterID;
        this.name = name;
    }

    public Module(int id, int semesterID, String name) {
        this.id = id;
        this.semesterID = semesterID;
        this.name = name;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getSemesterID() {
        return semesterID;
    }

    public String getName() {
        return name;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return id == module.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
