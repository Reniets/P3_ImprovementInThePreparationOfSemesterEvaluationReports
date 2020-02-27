package dbComponent.data;

import java.util.Objects;

public class Question {

    // Fields:
    private int id;
    private int semesterID;
    private String question;

    // Constructors:
    //Added for the purpose of using the fields of this class in another model, without having to instantiate them in advance
    protected Question() {

    }

    public Question(String question) {
        this.question = question;
    }

    public Question(int semesterID, String question) {
        this.semesterID = semesterID;
        this.question = question;
    }

    public Question(int id, int semesterID, String question) {
        this.id = id;
        this.semesterID = semesterID;
        this.question = question;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSemesterID(int semesterID) {
        this.semesterID = semesterID;
    }

    public int getId() {
        return this.id;
    }

    public int getSemesterID() {
        return this.semesterID;
    }

    public String getQuestion() {
        return this.question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return this.id == question.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
