package dbComponent.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnswerTest {

    // Test Fields:
    private static Answer answerEmpty;
    private static Answer answer;
    private static Answer answerB;

    @BeforeAll
    static void beforeAll() {
        answerEmpty = new Answer(); // Just checking that creating an empty Answer is possible
        answer = new Answer(0, 1, 2, "3");
        answerB = new Answer(0, 1, 2, "Irrelevant");
    }

    // Tests:
    @Test
    void getQuestionID() {
        assertEquals(0, answer.getQuestionID());
    }

    @Test
    void getRespondentID() {
        assertEquals(1, answer.getRespondentID());
    }

    @Test
    void getModuleID() {
        assertEquals(2, answer.getModuleID());
    }

    @Test
    void getAnswer() {
        assertEquals("3", answer.getAnswer());
    }

    @Test
    void equals() {
        assertEquals(answer, answerB);
    }

    @Test
    void hashCodeTest() {
        assertEquals(answer.hashCode(), answerB.hashCode());
    }
}