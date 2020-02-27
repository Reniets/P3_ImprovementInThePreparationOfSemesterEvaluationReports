package dbComponent.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    // Test Fields:
    private static Question questionEmpty;
    private static Question question;
    private static Question questionB;
    private static Question questionC;

    // Test Constant
    private static final String QUESTION = "is this the question?";

    @BeforeAll
    static void beforeAll() {
        questionEmpty = new Question();
        question = new Question(0, 1, QUESTION);
        questionB = new Question(1, QUESTION);
        questionC = new Question(QUESTION);
    }

    // Tests:
    @Test
    void getId() {
        assertEquals(0, question.getId());
    }

    @Test
    void getSemesterID() {
        assertEquals(1, question.getSemesterID());
    }

    @Test
    void getQuestion() {
        assertEquals(QUESTION, question.getQuestion());
    }

    // Tests
    @Test
    void setId() {
        questionB.setId(0);
        assertEquals(0, questionB.getId());
    }

    @Test
    void setSemesterID() {
        questionC.setSemesterID(1);
        assertEquals(1, questionC.getSemesterID());
    }

    @Test
    void equals() {
        assertEquals(question, questionB);
    }

    @Test
    void hashCodeTest() {
        assertEquals(question.hashCode(), questionB.hashCode());
    }
}