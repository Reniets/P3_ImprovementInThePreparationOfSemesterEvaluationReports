package dbComponent.data;

import dbComponent.enums.SemesterSeason;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructureDBTest {

    // Test Fields:
    private static StructureDB structure;

    // Test Constant:
    private static final String EMAIL = "test@mail.dk";
    private static final String NAME = "Name";

    @BeforeAll
    static void beforeAll() {
        structure = new StructureDB();
    }

    // Tests
    // Setters and Getters
    @Test
    void setAndGetCoordinator() {
        Coordinator coordinator = new Coordinator(0, EMAIL);
        structure.setCoordinator(coordinator);
        assertEquals(coordinator, structure.getCoordinator());
    }

    @Test
    void setSemester() {
        Semester semester = new Semester(0, 1, NAME, 3, SemesterSeason.AUTUMN);
        structure.setSemester(semester);
        assertEquals(semester, structure.getSemester());
    }

    @Test
    void getModules() {
        Module module = new Module();
        structure.getModules().add(module);
        assertEquals(module, structure.getModules().get(0));
    }

    @Test
    void getQuestions() {
        Question question = new Question();
        structure.getQuestions().add(question);
        assertEquals(question, structure.getQuestions().get(0));
    }

    @Test
    void getAnswers() {
        Answer answer = new Answer();
        structure.getAnswers().add(answer);
        assertEquals(answer, structure.getAnswers().get(0));
    }
}