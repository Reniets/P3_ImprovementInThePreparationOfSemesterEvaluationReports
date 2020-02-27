package LinearAlgebra.Types.Matrices.InternalStorageTypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrictMatrixStorageTest {

    // Test Field
    private StrictMatrixStorage sms;

    @BeforeEach
    void beforeEach() {
        sms = new StrictMatrixStorage(3, 3);
    }

    // Tests:
    @Test
    void getEntry() {
        assertEquals(0, sms.getEntry(0, 0));
    }

    @Test
    void getRows() {
        assertEquals(3, sms.getRows());
    }

    @Test
    void getCols() {
        assertEquals(3, sms.getCols());
    }

    @Test
    void setEntry() {
        sms.setEntry(0, 0, 1);
        assertEquals(1, sms.getEntry(0, 0));
    }

    @Test
    void additionToEntry() {
        sms.additionToEntry(0, 0, 1);
        assertEquals(1, sms.getEntry(0, 0));
    }

    @Test
    void subtractionToEntry() {
        sms.subtractionToEntry(0, 0, 1);
        assertEquals(-1, sms.getEntry(0, 0));
    }

    @Test
    void multiplicationToEntry() {
        sms.setEntry(0, 0, 2);
        sms.multiplicationToEntry(0, 0, 2);
        assertEquals(4, sms.getEntry(0, 0));
    }

    @Test
    void divisionToEntry() {
        sms.setEntry(0, 0, 2);
        sms.divisionToEntry(0, 0, 2);
        assertEquals(1, sms.getEntry(0, 0));
    }
}