package LinearAlgebra.Types.Matrices.InternalStorageTypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicMatrixStorageTest {

    // Test Field:
    private DynamicMatrixStorage dms;

    @BeforeEach
    void beforeEach() {
        this.dms = new DynamicMatrixStorage(3, 3);
    }

    // Tests:
    @Test
    void getEntry() {
        assertEquals(0, this.dms.getEntry(0, 0));
    }

    @Test
    void getRows() {
        assertEquals(3, this.dms.getRows());
    }

    @Test
    void getCols() {
        assertEquals(3, this.dms.getCols());
    }

    @Test
    void setEntry() {
        this.dms.setEntry(0, 0, 1);
        assertEquals(1, this.dms.getEntry(0, 0));
    }

    @Test
    void additionToEntry() {
        this.dms.additionToEntry(0, 0, 1);
        assertEquals(1, this.dms.getEntry(0, 0));
    }

    @Test
    void subtractionToEntry() {
        this.dms.subtractionToEntry(0, 0, 1);
        assertEquals(-1, this.dms.getEntry(0,0));
    }

    @Test
    void multiplicationToEntry() {
        this.dms.setEntry(0, 0, 2);
        this.dms.multiplicationToEntry(0, 0, 2);
        assertEquals(4, this.dms.getEntry(0, 0));
    }

    @Test
    void divisionToEntry() {
        this.dms.setEntry(0, 0, 2);
        this.dms.divisionToEntry(0, 0, 2);
        assertEquals(1, this.dms.getEntry(0, 0));
    }
}