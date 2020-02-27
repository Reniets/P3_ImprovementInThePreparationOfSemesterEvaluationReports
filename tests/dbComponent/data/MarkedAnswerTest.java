package dbComponent.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarkedAnswerTest {

    // Test Field:
    private MarkedAnswer markedAnswerEmpty;
    private MarkedAnswer markedAnswer;
    private MarkedAnswer markedAnswerDublicate;

    @BeforeEach
    void beforeEach() {
        this.markedAnswerEmpty = new MarkedAnswer();
        this.markedAnswer = new MarkedAnswer(0, 1, 2, 3);
        this.markedAnswerDublicate = new MarkedAnswer(0, 1, 2, 3);
    }

    // Tests:
    @Test
    void getQuestionID() {
        assertEquals(0, this.markedAnswer.getQuestionID());
    }

    @Test
    void getRespondantID() {
        assertEquals(1, this.markedAnswer.getRespondantID());
    }

    @Test
    void getModuleID() {
        assertEquals(2, this.markedAnswer.getModuleID());
    }

    @Test
    void getColour() {
        assertEquals(3, this.markedAnswer.getColour());
    }

    @Test
    void setColour() {
        this.markedAnswer.setColour(4);
        assertEquals(4, this.markedAnswer.getColour());
    }

    @Test
    void equals() {
        assertEquals(this.markedAnswer, this.markedAnswerDublicate);
    }

    @Test
    void hashCodeTest() {
        assertEquals(this.markedAnswer.hashCode(), this.markedAnswerDublicate.hashCode());
    }
}