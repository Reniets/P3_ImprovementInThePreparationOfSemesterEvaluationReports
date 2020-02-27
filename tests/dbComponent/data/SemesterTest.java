package dbComponent.data;

import dbComponent.enums.SemesterSeason;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SemesterTest {

    // Test Fields:
    private static Semester semesterEmpty;
    private static Semester semester;
    private static Semester semesterB;

    // Test Constant:
    private static final String NAME = "Name";

    @BeforeAll
    static void beforeAll() {
        semesterEmpty = new Semester();
        semester = new Semester(0, 1, NAME, 3, SemesterSeason.AUTUMN);
        semesterB = new Semester(1, NAME, 3, SemesterSeason.AUTUMN);
    }

    // Tests:
    @Test
    void getId() {
        assertEquals(0, semester.getId());
    }

    @Test
    void getCoordinatorID() {
        assertEquals(1, semester.getCoordinatorID());
    }

    @Test
    void getName() {
        assertEquals(NAME, semester.getName());
    }

    @Test
    void getYear() {
        assertEquals(3, semester.getYear());
    }

    @Test
    void getSeason() {
        assertEquals(SemesterSeason.AUTUMN, semester.getSeason());
    }

    @Test
    void setId() {
        semesterB.setId(0);
        assertEquals(0, semesterB.getId());
    }

    @Test
    void equals() {
        assertEquals(semester, semesterB);
    }

    @Test
    void hashCodeTest() {
        assertEquals(semester.hashCode(), semesterB.hashCode());
    }
}