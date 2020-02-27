package LinearAlgebra.Types.Decomposistions;

import LinearAlgebra.Statics.Enums.MatrixDirection;
import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Vectors.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QRDecompositionTest01 {

    // Test Fields:
    private static Matrix Q;
    private static Matrix R;
    private static Matrix matrix;

    // Test Constants:
    private static final int ROWS = 3;
    private static final int COLS = 3;

    @BeforeEach
    void beforeEach() {
        // A matrix with 3 linearly independent column vectors is required.
        double[][] matrixArray = new double[][]{
                {1, 1, 0},
                {1, 2, -1},
                {1, 0, 2}
        };

        matrix = new DenseMatrix(matrixArray);
        QRDecomposition QR = new QRDecomposition(matrix);

        Q = QR.getQ();
        R = QR.getR();
    }

    // Tests:
    @Test
    void A_Equals_QR() {
        // Calculate Q * R
        Matrix newMatrix = Q.mult(R);

        // Check if each row of the matrices are equal (+/- delta)
        for (int row = 0; row < ROWS; row++) {
            assertArrayEquals(matrix.getRowVector(row).toArray(), newMatrix.getRowVector(row).toArray(), 0.0000001);
        }
    }

    @Test
    void RIsUpperTriangular() {
        assertTrue(Matrices.isTriangular(R, MatrixDirection.UPPER));
    }

    @RepeatedTest(COLS)
    void QIsUnitVectors01(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition() - 1;

        assertEquals(1, Q.getColumnVector(rep).norm(2), 0.000000001);
    }

    @RepeatedTest(ROWS)
    void QIsUnitVectors02(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition() - 1;

        assertEquals(1, Q.getRowVector(rep).norm(2), 0.000000001);
    }

    @RepeatedTest(COLS - 1)
    void QHasIndependentVectors(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition() - 1;

        Vector vectorRep = Q.getColumnVector(rep);

        for (int i = rep + 1; i < COLS; i++) {
            Vector vectorI = Q.getColumnVector(i);
            assertEquals(0, vectorRep.dotProduct(vectorI), 0.000000001);
        }
    }

    @Test
    void QIsOrthogonal01() {
        assertTrue(Q.getRows() == Q.getColumns());
    }

    @Test
    void QIsOrthogonal02() {
        Matrix identity = Matrices.getIdentityMatrix(ROWS);
        // Calculate Q * Q_Transposed.
        Matrix newMatrix = Q.multTrans(Q);

        // Check if each row of the matrices are equal (+/- delta)
        for (int row = 0; row < ROWS; row++) {
            assertArrayEquals(identity.getRowVector(row).toArray(), newMatrix.getRowVector(row).toArray(), 0.0000001);
        }
    }
}