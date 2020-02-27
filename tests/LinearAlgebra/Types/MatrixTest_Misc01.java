package LinearAlgebra.Types;

import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest_Misc01 {

    // Test Fields
    private static Matrix matrix;
    private static double[][] matrixArray;

    @BeforeEach
    void beforeEach() {
        matrixArray = new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        matrix = new DenseMatrix(matrixArray);
    }

    // Tests:
    @Test
    void toString01() {
        String expected = "DenseMatrix:\n" +
                "[[1.0, 2.0, 3.0]\n" +
                " [4.0, 5.0, 6.0]\n" +
                " [7.0, 8.0, 9.0]]";

        assertEquals(expected, matrix.toString());


        Matrix matrix = new MatrixBuilder(matrixArray, true)
                .setEntry(1, 1, 1000.245)
                .build();

        expected = "DenseMatrix:\n" +
                "[[1.0, 2.0,      3.0]\n" +
                " [4.0, 1000.245, 6.0]\n" +
                " [7.0, 8.0,      9.0]]";

        assertEquals(expected, matrix.toString());

    }

    @Test
    void clone01() {
        assertEquals(matrix, matrix.clone());
    }

    @Test
    void clone02() {
        assertEquals(matrix.hashCode(), matrix.clone().hashCode());
    }

    @Test
    void equalsTest_Symmetric01() {
        Matrix x = matrix;
        Matrix y = matrix.clone();

        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }

    @Test
    void equalsTest_Symmetric02() {
        Matrix x = matrix;

        Matrix y = new MatrixBuilder(x, true)
                .setEntry(0, 0, -99)
                .build();

        assertFalse(x.equals(y) || y.equals(x));
        assertFalse(x.hashCode() == y.hashCode());
    }

    @Test
    void equalsTest_Symmetric03() {
        Matrix y = matrix.clone();

        double[][] newMatrixArray = new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        Matrix newMatrix = new DenseMatrix(newMatrixArray);

        assertTrue(newMatrix.equals(y) && y.equals(newMatrix));
        assertTrue(newMatrix.hashCode() == y.hashCode());
    }

    @Test
    void constructor01() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DenseMatrix(null));
    }

    @Test
    void constructor02() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DenseMatrix(new double[][]{}));
    }

    @Test
    void constructor03() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DenseMatrix(new double[][]{{}, {}}));
    }
}