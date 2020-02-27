package LinearAlgebra.Statics;

import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatricesTestEcholonForm {

    // Test Fields:
    private static Matrix matrix_echoRow;
    private static Matrix matrix;

    @BeforeEach
    void beforeEach() {
        double[][] matrixArray_echoRow = new double[][]{
                {0, 2, 3, 1},
                {0, 0, 2, 9},
                {0, 0, 0, 0}
        };

        double[][] matrixArray = new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        matrix_echoRow = new DenseMatrix(matrixArray_echoRow);
        matrix = new DenseMatrix(matrixArray);
    }

    // Tests:
    @Test
    void isEchelonForm01() {
        assertTrue(Matrices.isEchelonForm(matrix_echoRow));
    }

    @Test
    void isEchelonForm02() {
        assertFalse(Matrices.isEchelonForm(matrix));
    }
}