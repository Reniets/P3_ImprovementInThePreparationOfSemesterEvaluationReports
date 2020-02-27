package Models;

import dbComponent.Models.SemesterModel;
import dbComponent.enums.SemesterSeason;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SemesterModelTest {

    // Test Field:
    private static SemesterModel semesterModel;

    @BeforeAll
    static void beforeAll() {
        semesterModel = new SemesterModel(0, 0, "test", 2018, SemesterSeason.AUTUMN);
        semesterModel.loadRelatedData();
    }

    @Test
    void loadRelatedData() {

    }

    @Test
    void getDisplayName01() {
        assertEquals("test", this.semesterModel.getDisplayName());
    }

    @Test
    void getDisplayName02() {
        SemesterModel semesterModelLongName = new SemesterModel(0, 0, "abcdefghijklmnopqrstuvxyzæøå+2", 2018, SemesterSeason.AUTUMN);
        assertEquals("abcdefghijklmnopqrstuvxyzæøå", semesterModelLongName.getDisplayName());
    }

    @Test
    void getModules() {

    }

    @Test
    void getQuestions() {
    }

    @Test
    void getAnswers() {
    }

    @Test
    void attachAnswer() {
    }

    @Test
    void attachMarkedAnswer() {
    }
}