package LinearAlgebra.Types;

import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Vectors.DenseVector;
import LinearAlgebra.Types.Vectors.Vector;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest_Math01 {

    // Test Fields:
    private static Matrix matrix;

    // Test Constants
    private static final int ROWS = 3;
    private static final int COLS = 3;

    @BeforeEach
    void beforeEach() {
        double[][] matrixArray = new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        matrix = new DenseMatrix(matrixArray);
    }

    // Tests:
    @Test
    void mult_Matrix01() {
        Matrix expected = new DenseMatrix(ROWS, COLS);

        assertEquals(expected, matrix.mult(new DenseMatrix(ROWS, COLS)));
    }

    @Test
    void mult_Matrix02() {
        Matrix expected = matrix.clone();

        assertEquals(expected, matrix.mult(Matrices.getIdentityMatrix(ROWS)));
    }

    @Test
    void mult_Matrix03() {
        double[][] expectedMatrix = {
                {30, 36, 42},
                {66, 81, 96},
                {102, 126, 150}
        };
        Matrix expected = new DenseMatrix(expectedMatrix);

        assertEquals(expected, matrix.mult(matrix));
    }

    @Test
    void mult_Matrix04() {
        double[][] newMatrix = {
                {1},
                {2},
                {3}
        };

        double[][] expectedMatrix = {
                {14},
                {32},
                {50}
        };
        Matrix expected = new DenseMatrix(expectedMatrix);

        assertEquals(expected, matrix.mult(new DenseMatrix(newMatrix)));
    }

    @Test
    void mult_Matrix05() {
        double[][] newMatrix = {
                {1, 2, 3}
        };

        double[][] expectedMatrix = {
                {30, 36, 42}
        };
        Matrix expected = new DenseMatrix(expectedMatrix);

        assertEquals(expected, new DenseMatrix(newMatrix).mult(matrix));
    }

    @Test
    void mult_Matrix06() {
        Matrix newMatrix = new DenseMatrix(new double[][]{{1, 2, 3, 4}}); // Real matrix is 3x3 and therefor not compatible

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.mult(newMatrix));
    }

    @Test
    void mult_Vector() {
        Vector vector = new DenseVector(new double[]{1, 2, 3});

        double[][] expectedMatrix = {
                {14},
                {32},
                {50}
        };
        Matrix expected = new DenseMatrix(expectedMatrix);

        assertEquals(expected, matrix.mult(vector));
    }

    @Test
    void mult_Scaler() {
        double scaler = 7;

        Matrix res = matrix.mult(scaler);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                assertEquals(matrix.toArray()[row][col] * scaler, res.toArray()[row][col]);
            }
        }
    }

    @Test
    void sub01() {
        Matrix subMatrix = Matrices.randomMatrix(ROWS, COLS, -1, 1);
        Matrix res = matrix.sub(subMatrix);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                double subRes = matrix.toArray()[row][col] - subMatrix.toArray()[row][col];

                assertEquals(subRes, res.toArray()[row][col]);
            }
        }
    }

    @Test
    void sub02() {
        Matrix subMatrix = Matrices.randomMatrix(ROWS + 1, COLS + 1, -1, 1);

        // Subtract incompatible matrix.
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.sub(subMatrix));
    }

    @Test
    void sub03() {
        Matrix subMatrix = Matrices.randomMatrix(ROWS - 1, COLS - 1, -1, 1);

        // Subtract incompatible matrix.
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.sub(subMatrix));
    }

    @Test
    void sub04() {
        Matrix subMatrix = Matrices.randomMatrix(ROWS, COLS - 1, -1, 1);

        // Subtract incompatible matrix.
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.sub(subMatrix));
    }

    @Test
    void sub05() {
        Matrix subMatrix = Matrices.randomMatrix(ROWS - 1, COLS, -1, 1);

        // Subtract incompatible matrix.
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.sub(subMatrix));
    }

    @Test
    void add01() {
        Matrix addMatrix = Matrices.randomMatrix(ROWS, COLS, -1, 1);
        Matrix res = matrix.add(addMatrix);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                double addRes = matrix.toArray()[row][col] + addMatrix.toArray()[row][col];

                assertEquals(addRes, res.toArray()[row][col]);
            }
        }
    }

    @Test
    void add02() {
        Matrix addMatrix = Matrices.randomMatrix(ROWS + 1, COLS + 1, -1, 1);

        // Add incompatible matrix.
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.add(addMatrix));
    }

    @Test
    void add03() {
        Matrix addMatrix = Matrices.randomMatrix(ROWS - 1, COLS - 1, -1, 1);

        // Add incompatible matrix.
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.add(addMatrix));
    }

    @Test
    void add04() {
        Matrix addMatrix = Matrices.randomMatrix(ROWS, COLS - 1, -1, 1);

        // Add incompatible matrix.
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.add(addMatrix));
    }

    @Test
    void add05() {
        Matrix addMatrix = Matrices.randomMatrix(ROWS - 1, COLS, -1, 1);

        // Add incompatible matrix.
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.add(addMatrix));
    }

    @Test
    void transpose01() {
        Matrix transposed = matrix.transpose();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                assertEquals(matrix.toArray()[row][col], transposed.toArray()[col][row]);
            }
        }
    }

    @Test
    void transpose02() {
        assertEquals(matrix, matrix.transpose().transpose());
    }

    // Transpose with non square matrix; More cols than rows
    @Test
    void transpose03() {
        Matrix newMatrix = Matrices.randomMatrix(2, 5, -10, 10);
        Matrix transposed = newMatrix.transpose();

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(newMatrix.toArray()[row][col], transposed.toArray()[col][row]);
            }
        }
    }

    // Transpose with non square matrix; More rows than cols
    @Test
    void transpose04() {
        Matrix newMatrix = Matrices.randomMatrix(5, 2, -10, 10);
        Matrix transposed = newMatrix.transpose();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 2; col++) {
                assertEquals(newMatrix.toArray()[row][col], transposed.toArray()[col][row]);
            }
        }
    }

    @Test
    void transpose05() {
        Matrix newMatrix = Matrices.randomMatrix(5, 2, -10, 10);
        assertEquals(newMatrix, newMatrix.transpose().transpose());
    }

    @Test
    void transpose06() {
        Matrix newMatrix = Matrices.randomMatrix(2, 5, -10, 10);
        assertEquals(newMatrix, newMatrix.transpose().transpose());
    }

    @RepeatedTest(10)
    void pow01(RepetitionInfo repetitionInfo) {
        int powerOf = repetitionInfo.getCurrentRepetition();

        Matrix res = matrix;

        for (int i = 1; i < powerOf; i++) {
            res = res.mult(matrix);
        }

        assertEquals(matrix.pow(powerOf), res);
    }

    @Test
    void pow02() {
        Matrix res = matrix.mult(matrix);
        assertEquals(res, matrix.pow(2));
    }

    @Test
    void pow03() {
        Matrix res = matrix.mult(matrix).mult(matrix);
        assertEquals(res, matrix.pow(3));
    }

    @RepeatedTest(10)
    void pow04(RepetitionInfo repetitionInfo) {
        int index = repetitionInfo.getCurrentRepetition();
        Matrix matrix = Matrices.randomMatrix(index, index);

        assertEquals(Matrices.getIdentityMatrix(index), matrix.pow(0));
    }

    @Test
    void compDivision_Matrix01() {
        double constant = 2;

        Matrix divMatrix = new DenseMatrix(ROWS, COLS, constant);
        Matrix res = matrix.compDivision(divMatrix);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                assertEquals(matrix.toArray()[row][col] / constant, res.toArray()[row][col]);
            }
        }
    }

    @Test
    void compDivision_Matrix02() {
        Matrix multMatrix = Matrices.randomMatrix(ROWS, COLS, 1, 10);
        Matrix res = matrix.compDivision(multMatrix);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                double expected = matrix.toArray()[row][col] / multMatrix.toArray()[row][col];
                assertEquals(expected, res.toArray()[row][col]);
            }
        }
    }

    @Test
    void compDivision_Matrix03() {
        Matrix multMatrix = Matrices.randomMatrix(ROWS + 1, COLS, 1, 10);

        // Comp div a incompatible matrix
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.compDivision(multMatrix));
    }

    @Test
    void compDivision_Scalar01() {
        double constant = 2;

        Matrix res = matrix.compDivision(constant);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                assertEquals(matrix.toArray()[row][col] / constant, res.toArray()[row][col]);
            }
        }
    }

    @Test
    void compMult01() {
        double constant = 2;

        Matrix multMatrix = new DenseMatrix(ROWS, COLS, constant);
        Matrix res = matrix.compMult(multMatrix);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                assertEquals(matrix.toArray()[row][col] * constant, res.toArray()[row][col]);
            }
        }
    }

    @Test
    void compMult02() {
        Matrix multMatrix = Matrices.randomMatrix(ROWS, COLS, 1, 10);
        Matrix res = matrix.compMult(multMatrix);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                double expected = matrix.toArray()[row][col] * multMatrix.toArray()[row][col];
                assertEquals(expected, res.toArray()[row][col]);
            }
        }
    }

    @Test
    void compMult03() {
        Matrix multMatrix = Matrices.randomMatrix(ROWS + 1, COLS, 1, 10);

        // Comp mult a incompatible matrix
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.compMult(multMatrix));
    }

    @Test
    void transMult01() {    // With two known non-negative matrices
        assertEquals(matrix.transpose().mult(matrix), matrix.transMult(matrix));
    }

    @Test
    void transMult02() {    // With two random matrices
        Matrix A = Matrices.randomMatrix(3, 3, -10, 10);
        Matrix B = Matrices.randomMatrix(3, 3, -10, 10);

        assertEquals(A.transpose().mult(B), A.transMult(B));
    }

    @Test
    void transMult03() {    // With two non-compatible matrices
        Matrix A = new DenseMatrix(1, 1);
        Matrix B = new DenseMatrix(2, 2);

        Assertions.assertThrows(IllegalArgumentException.class, () -> A.transMult(B));
    }

    @Test
    void multTrans01() {    // With two known non-negative matrices
        assertEquals(matrix.mult(matrix.transpose()), matrix.multTrans(matrix));
    }

    @Test
    void multTrans02() {    // With two random matrices
        Matrix A = Matrices.randomMatrix(3, 3, -10, 10);
        Matrix B = Matrices.randomMatrix(3, 3, -10, 10);

        assertEquals(A.mult(B.transpose()), A.multTrans(B));
    }

    @Test
    void multTrans03() {    // With two non-compatible matrices
        Matrix A = new DenseMatrix(1, 1);
        Matrix B = new DenseMatrix(2, 2);

        Assertions.assertThrows(IllegalArgumentException.class, () -> A.multTrans(B));
    }

    @Test
    void multTrans04() {    // With two non square matrices
        Matrix A = Matrices.randomMatrix(2, 3, -10, 10);
        Matrix B = Matrices.randomMatrix(4, 3, -10, 10);

        assertEquals(A.mult(B.transpose()), A.multTrans(B));
    }
}