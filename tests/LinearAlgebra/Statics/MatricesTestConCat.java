package LinearAlgebra.Statics;

import LinearAlgebra.Types.Matrices.Matrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatricesTestConCat {

    // Test Fields:
    private Matrix matrixA = Matrices.constantEntry(3, 3, A);
    private Matrix matrixB = Matrices.constantEntry(3, 3, B);

    // Test Constants:
    private static final char A = 'A';
    private static final char B = 'B';

    // Tests:
    @Test
    void concatRows() {
        Matrix matrixC = Matrices.concatRows(this.matrixA, this.matrixB);
        entryAssertion(matrixC);
    }

    @Test
    void concatRowsException() {
        Matrix matrixExtra = Matrices.constantEntry(4, 4, A);

        assertThrows(
                IllegalArgumentException.class, () -> Matrices.concatRows(this.matrixA, matrixExtra)
        );
    }

    @Test
    void concatColumns() {
        Matrix matrixC = Matrices.concatColumns(this.matrixA, this.matrixB);
        entryAssertion(matrixC);
    }

    @Test
    void concatColumnsException() {
        Matrix matrixExtra = Matrices.constantEntry(4, 4, A);

        assertThrows(
                IllegalArgumentException.class, () -> Matrices.concatColumns(this.matrixA, matrixExtra)
        );
    }

    // Test Method:
    private void entryAssertion(Matrix matrix) {
        for (int r = 0; r < matrix.getRows(); r++) {
            for (int c = 0; c < matrix.getColumns(); c++) {
                if (r <= 2 && c <= 2) {
                    assertTrue(matrix.getEntry(r, c) == A);
                } else {
                    assertTrue(matrix.getEntry(r, c) == B);
                }
            }
        }
    }

}