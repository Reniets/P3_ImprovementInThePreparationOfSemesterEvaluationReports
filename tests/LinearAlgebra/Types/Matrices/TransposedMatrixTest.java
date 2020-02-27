package LinearAlgebra.Types.Matrices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransposedMatrixTest {

    // Test Fields:
    private Matrix parent;
    private TransposedMatrix matrixT;

    @BeforeEach
    void beforeEach() {
        this.parent = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .addRow(7, 8, 9)
                .build();

        this.matrixT = new TransposedMatrix(parent);
    }

    // Tests:
    @Test
    void getRows() {
        assertEquals(3, matrixT.getRows());
    }

    @Test
    void getColumns() {
        assertEquals(3, matrixT.getColumns());
    }

    @Test
    void getEntry() {
        assertEquals(7, matrixT.getEntry(0, 2));
    }

    @Test
    void transposeSoft() {
        assertEquals(parent, matrixT.transposeSoft());
    }
}