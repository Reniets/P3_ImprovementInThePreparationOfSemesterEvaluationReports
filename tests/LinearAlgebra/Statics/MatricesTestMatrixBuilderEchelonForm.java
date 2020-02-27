package LinearAlgebra.Statics;

import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatricesTestMatrixBuilderEchelonForm {

    @Test
    void isEchelonForm() {
        Matrix matrix000 = new MatrixBuilder()
                .addRow(3, 0, 0)
                .addRow(0, 1, 5)
                .addRow(0, 0, 10)
                .build();
        assertTrue(Matrices.isEchelonForm(matrix000));

        Matrix matrix001 = new MatrixBuilder()
                .addRow(0, 0, 0)
                .addRow(0, 1, 5)
                .addRow(0, 0, 10)
                .addRow(0, 0, 0, 1)
                .build();

        assertFalse(Matrices.isEchelonForm(matrix001));

        Matrix matrix002 = new MatrixBuilder()
                .addRow(1, 0, 0)
                .addRow(0, 0, 5)
                .addRow(0, 0, 10)
                .addRow(0, 0, 0, 1)
                .build();

        assertFalse(Matrices.isEchelonForm(matrix002));

        Matrix matrix003 = new MatrixBuilder()
                .addRow(3, 0, 0, 0)
                .addRow(0, 1, 5)
                .addRow(0, 0, 10)
                .build();
        assertTrue(Matrices.isEchelonForm(matrix003));
    }

    @Test
    void gaussElimination() {
        Matrix matrixNonEchelon = new MatrixBuilder()
                .addRow(0, 0, 0)
                .addRow(0, 1, 1)
                .addRow(0, 5, 7)
                .build();

        assertTrue(Matrices.isEchelonForm(Matrices.gaussElimination(matrixNonEchelon)));
    }

    @Test
    void gaussJordanElimination01() {
        Matrix matrixNonEchelon = new MatrixBuilder()
                .addRow(0, 0, 0)
                .addRow(0, 5, 0)
                .addRow(0, 0, 2)
                .build();

        assertTrue(Matrices.isEchelonForm(Matrices.gaussJordanElimination(matrixNonEchelon)));
    }

    @Test
    void gaussJordanElimination02() {
        Matrix matrixNonEchelon = new MatrixBuilder()
                .addRow(4, 2, 0, 2, 3)
                .addRow(0, 9, 1, 5, 8)
                .addRow(0, 0, 0, 5, 2)
                .addRow(0, 2, 0, 4, 2)
                .addRow(0, 0, 5, 5, 1)
                .build();

        assertTrue(Matrices.isEchelonForm(Matrices.gaussJordanElimination(matrixNonEchelon)));
    }
}