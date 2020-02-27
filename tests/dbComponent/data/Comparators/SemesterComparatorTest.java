package dbComponent.data.Comparators;

import dbComponent.data.Semester;
import dbComponent.enums.SemesterSeason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SemesterComparatorTest {

    // Test Fields:
    private Semester semesterA;
    private Semester semesterB;
    private Semester semesterPrevYear;
    private SemesterComparator comparator;

    @BeforeEach
    void beforeEach() {
        this.semesterA = new Semester(0, "test", 2018, SemesterSeason.AUTUMN);
        this.semesterB = new Semester(0, "test", 2018, SemesterSeason.SPRING);
        this.semesterPrevYear = new Semester(0, "test", 2017, SemesterSeason.AUTUMN);
        this.comparator = new SemesterComparator();
    }

    // Tests:
    // At the same time
    @Test
    void compare01() {
        int expected = 0;
        int result = this.comparator.compare(this.semesterA, this.semesterA);
        assertEquals(expected, result);
    }

    @Test
    // Different seasons
    void compare02() {
        int expected = -1;
        int result = this.comparator.compare(this.semesterA, this.semesterB);
        assertEquals(expected, result);
    }

    // Different seasons ~ different order
    @Test
    void compare03() {
        int expected = 1;
        int result = this.comparator.compare(this.semesterB, this.semesterA);
        assertEquals(expected, result);
    }

    // Different year
    @Test
    void compare04() {
        int expected = -1;
        int result = this.comparator.compare(this.semesterA, this.semesterPrevYear);
        assertEquals(expected, result);
    }
}