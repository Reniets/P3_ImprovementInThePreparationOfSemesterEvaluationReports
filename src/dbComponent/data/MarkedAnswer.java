package dbComponent.data;

import java.util.Objects;

public class MarkedAnswer {

    //Fields
    private int QuestionID;
    private int RespondantID;
    private int ModuleID;
    private int Colour;

    // Constructors:
    //Added for the purpose of using the fields of this class in another model, without having to instantiate them in advance
    protected MarkedAnswer() {

    }

    public MarkedAnswer(int QuestionID, int RespondantID, int ModuleID, int Colour) {
        this.Colour = Colour;
        this.ModuleID = ModuleID;
        this.QuestionID = QuestionID;
        this.RespondantID = RespondantID;
    }

    //Getters
    public int getQuestionID() {
        return QuestionID;
    }

    public int getRespondantID() {
        return RespondantID;
    }

    public int getModuleID() {
        return ModuleID;
    }

    public int getColour() {
        return Colour;
    }

    //Setters
    public void setColour(int colour) {
        Colour = colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkedAnswer that = (MarkedAnswer) o;
        return QuestionID == that.QuestionID &&
                RespondantID == that.RespondantID &&
                ModuleID == that.ModuleID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(QuestionID, RespondantID, ModuleID);
    }
}
