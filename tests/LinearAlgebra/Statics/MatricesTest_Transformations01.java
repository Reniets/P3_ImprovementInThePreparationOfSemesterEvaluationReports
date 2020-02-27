package LinearAlgebra.Statics;

import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Vectors.Vector;
import LinearAlgebra.Types.Vectors.VectorBuilder;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatricesTest_Transformations01 {

    // Test Fields:
    private static Matrix matrix = new DenseMatrix(new double[][]{
            {1, 2},
            {4, 5},
            {0, 8}
    });

    private static Matrix gramSchmidt = Matrices.gramSchmidt(matrix);

    // Tests:
    // Test that the resulting vectors are unit vectors
    @RepeatedTest(2)
    void gramSchmidt01(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition() - 1;

        assertEquals(1, gramSchmidt.getColumnVector(rep).norm(2), 0.00000001);
    }

    // Test that the resulting vectors are orthogonal
    @RepeatedTest(2)
    void gramSchmidt02(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition() - 1;

        Vector columnVector = gramSchmidt.getColumnVector(rep);

        for (int col = rep + 1; col < matrix.getColumns(); col++) {
            assertEquals(0, columnVector.dotProduct(gramSchmidt.getColumnVector(col)), 0.00000001);
        }
    }

    // Test that the span remains the same
    @Test
    void gramSchmidt03() {
        Matrix combinedMatrix = MatrixBuilder.buildEmpty(4, 3);

        // Start matrix
        for (int i = 0; i < 2; i++) {
            combinedMatrix = combinedMatrix.setRowVector(matrix.getColumnVector(i), i);
        }

        // End matrix
        for (int i = 0; i < 2; i++) {
            combinedMatrix = combinedMatrix.setRowVector(gramSchmidt.getColumnVector(i), i + 2);
        }

        // Get rank of matrices
        int rankOfStartMatrix = this.calculateRankOfMatrix(matrix.transpose());
        int rankOfEndMatrix = this.calculateRankOfMatrix(gramSchmidt.transpose());
        int rankOfCombinedMatrix = this.calculateRankOfMatrix(combinedMatrix);

        // Assert that: Rank(Start) = Rank(End) = Rank(Combined)
        assertTrue(rankOfStartMatrix == rankOfEndMatrix && rankOfEndMatrix == rankOfCombinedMatrix);

    }

    private int calculateRankOfMatrix(Matrix A) {
        int rank = 0;
        Matrix gauss = Matrices.gaussElimination(A);

        for (int i = 0; i < gauss.getRows(); i++) {
            if (!gauss.getRowVector(i).isNull()) {
                rank++;
            }
        }

        return rank;
    }

    @Test
    void getDiagonalMatrix() {
        Matrix diagonal = new MatrixBuilder()
                .addRow(matrix.getEntry(0, 0), 0)
                .addRow(0, matrix.getEntry(1, 1))
                .build();

        assertEquals(diagonal, Matrices.getDiagonalMatrix(matrix));
    }

    @Test
    void getDiagonalVector() {
        Vector diagonal = new VectorBuilder()
                .addEntries(matrix.getEntry(0, 0))
                .addEntries(matrix.getEntry(1, 1))
                .build();

        assertEquals(diagonal, Matrices.getDiagonalVector(matrix));
    }
}