package LinearAlgebra.Statics;

import LinearAlgebra.Statics.Enums.MatrixDirection;
import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Matrices.OnehotMatrix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatricesTestMisc {

    // Test fields:
    private static Matrix defaultMatrix = new DenseMatrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    });

    private static Matrix upperTriangle = new DenseMatrix(new double[][]{
            {0, 2, 3},
            {0, 5, 6},
            {0, 0, -2}
    });

    private static Matrix lowerTriangle = new DenseMatrix(new double[][]{
            {1, 0, 0},
            {-6, -8, 0},
            {7, 8, 9}
    });

    // Tests
    @Test
    void sumRows() {
        Matrix summedRows = Matrices.sumRows(defaultMatrix);

        double[] expected = {6, 15, 24};

        assertArrayEquals(expected, summedRows.getColumnVector(0).toArray());
    }

    @Test
    void sumColumns() {
        Matrix summedRows = Matrices.sumColumns(defaultMatrix);

        double[] expected = {12, 15, 18};

        assertArrayEquals(expected, summedRows.getRowVector(0).toArray());
    }

    @Test
    void sumMatrix() {
        double summedMatrix = Matrices.sumMatrix(defaultMatrix);

        assertEquals(45, summedMatrix);
    }

    @Test
    void isTriangular01() {
        assertFalse(Matrices.isTriangular(defaultMatrix, MatrixDirection.UPPER));
    }

    @Test
    void isTriangular02() {
        assertFalse(Matrices.isTriangular(defaultMatrix, MatrixDirection.LOWER));
    }

    @Test
    void isTriangular03() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Matrices.isTriangular(defaultMatrix, MatrixDirection.ROW));
    }

    @Test
    void isTriangular04() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Matrices.isTriangular(defaultMatrix, MatrixDirection.COL));
    }

    @Test
    void isTriangular05() {
        assertTrue(Matrices.isTriangular(upperTriangle, MatrixDirection.UPPER));
    }

    @Test
    void isTriangular06() {
        assertTrue(Matrices.isTriangular(lowerTriangle, MatrixDirection.LOWER));
    }

    @Test
    void isTriangular07() {
        assertFalse(Matrices.isTriangular(upperTriangle, MatrixDirection.LOWER));
    }

    @Test
    void isTriangular08() {
        assertFalse(Matrices.isTriangular(lowerTriangle, MatrixDirection.UPPER));
    }

    @Test
    void swapRows01() {
        // Get Expected
        Matrix expected = new MatrixBuilder(defaultMatrix, true)
                .setRow(2, defaultMatrix.getRowVector(0))
                .setRow(0, defaultMatrix.getRowVector(2))
                .build();


        Matrix A = defaultMatrix.clone();
        A = Matrices.swapRows(A, 0, 2);


        assertEquals(expected, A);
    }

    @Test
    void swapRows02() {
        Matrix A = Matrices.swapRows(defaultMatrix.clone(), 0, 2);

        assertFalse(defaultMatrix.equals(A));
    }

    @Test
    void swapColumns01() {
        // Get Expected
        Matrix expected = new MatrixBuilder(defaultMatrix, true)
                .setRow(2, defaultMatrix.getRowVector(0))
                .setRow(0, defaultMatrix.getRowVector(2))
                .build();

        Matrix A = defaultMatrix.clone();
        A = Matrices.swapRows(A, 0, 2);

        assertEquals(expected, A);
    }

    @Test
    void swapColumns02() {
        Matrix A = Matrices.swapColumns(defaultMatrix.clone(), 0, 2);

        assertFalse(defaultMatrix.equals(A));
    }

    @Test
    void testPartialMatrix01() {
        assertEquals(defaultMatrix,
                Matrices.getPartialMatrix(defaultMatrix, 0, 0, 3, 3));
    }

    @Test
    void testPartialMatrix02() {
        Matrix A = Matrices.getPartialMatrix(defaultMatrix, 1, 1, 2, 2);

        assertTrue(A.equals(
                new MatrixBuilder()
                        .addRow(5)
                        .build()
        ));
    }

    @Test
    void testPartialMatrix03() {
        Matrix A = Matrices.getPartialMatrix(defaultMatrix, 1, 1, 3, 3);

        assertTrue(A.equals(
                new MatrixBuilder()
                        .addRow(5, 6)
                        .addRow(8, 9)
                        .build()
        ));
    }

    @Test
    void testPartialMatrix04() {
        Matrix A = Matrices.getPartialMatrix(defaultMatrix, 0, 0, 2, 2);

        assertTrue(A.equals(
                new MatrixBuilder()
                        .addRow(1, 2)
                        .addRow(4, 5)
                        .build()
        ));
    }

    @Test
    void testPartialMatrix05() {
        assertThrows((IllegalArgumentException.class), () -> Matrices.getPartialMatrix(defaultMatrix, 0, 0, 1, 4));
    }

    @Test
    void testPartialMatrix06() {
        assertThrows((IllegalArgumentException.class), () -> Matrices.getPartialMatrix(defaultMatrix, -1, 0, 1, 3));
    }

    @Test
    void testPartialMatrix07() {
        assertThrows((IllegalArgumentException.class), () -> Matrices.getPartialMatrix(defaultMatrix, 2, 2, 1, 1));
    }

    @Test
    void testPartialMatrix08() {
        Matrix identityMatrix = Matrices.getIdentityMatrix(3);

        Matrices.getPartialMatrix(identityMatrix, 0, 0, identityMatrix.getRows(), identityMatrix.getColumns());
        assertTrue(Matrices.getPartialMatrix(identityMatrix, 0, 0, identityMatrix.getRows(), identityMatrix.getColumns()) instanceof OnehotMatrix);
    }
}