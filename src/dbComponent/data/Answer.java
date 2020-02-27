package dbComponent.data;

import java.util.Objects;

public class Answer {

    // Fields:
    private int questionID;
    private int respondentID;
    private int moduleID;
    private String answer;

    // Constructors:
    //Added for the purpose of using the fields of this class in another model, without having to instantiate them in advance
    protected Answer() {

    }

    public Answer(int questionID, int respondentID, int moduleID, String answer) {
        this.questionID = questionID;
        this.respondentID = respondentID;
        this.moduleID = moduleID;
        this.answer = answer;
    }

    // Getters:
    public int getQuestionID() {
        return questionID;
    }

    public int getRespondentID() {
        return respondentID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public String getAnswer() {
        return this.answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return questionID == answer.questionID &&
                respondentID == answer.respondentID &&
                moduleID == answer.moduleID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionID, respondentID, moduleID);
    }
}
