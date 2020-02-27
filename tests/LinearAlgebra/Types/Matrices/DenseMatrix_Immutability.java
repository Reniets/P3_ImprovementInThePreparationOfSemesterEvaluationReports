package LinearAlgebra.Types.Matrices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class DenseMatrix_Immutability {

    // Tests:
    @Test
    void CantUseToArrayToChangeContents() {
        Matrix matrix = new MatrixBuilder()
                .addRow(1, 2, 3)
                .addRow(4, 5, 6)
                .addRow(7, 8, 9)
                .build();

        matrix.toArray()[0][0] = -1;

        assertTrue(matrix.getEntry(0, 0) == 1);
    }
}