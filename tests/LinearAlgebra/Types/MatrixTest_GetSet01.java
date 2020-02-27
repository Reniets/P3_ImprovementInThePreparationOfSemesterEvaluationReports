package LinearAlgebra.Types;

import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Vectors.DenseVector;
import LinearAlgebra.Types.Vectors.Vector;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest_GetSet01 {

    // Test Fields:
    private static Matrix matrix;
    private static double[][] matrixArray;

    // Test Constants:
    private static final int ROWS = 3;
    private static final int COLS = 3;

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
    void getRows() {
        assertEquals(ROWS, matrix.getRows());
    }

    @Test
    void getColumns() {
        assertEquals(COLS, matrix.getColumns());
    }

    @Test
    void getColumnVector01() {
        for (int colID = 0; colID < COLS; colID++) {
            Vector colVector = matrix.getColumnVector(colID);

            double[] colArray = new double[ROWS];

            for (int i = 0; i < ROWS; i++) {
                colArray[i] = matrixArray[i][colID];
            }

            assertArrayEquals(colVector.toArray(), colArray);
        }
    }

    @Test
    void getColumnVector02() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.getColumnVector(4); // There are only 3 columns in the matrix.
        });
    }

    @Test
    void getColumnVector03() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.getColumnVector(-1); // You cant have negative columns
        });
    }

    @Test
    void getRowVector01() {
        for (int rowID = 0; rowID < ROWS; rowID++) {
            Vector rowVector = matrix.getRowVector(rowID);

            assertArrayEquals(rowVector.toArray(), matrixArray[rowID]);
        }
    }

    @Test
    void getRowVector02() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.getRowVector(4); // There are only 3 rows in the matrix.
        });
    }

    @Test
    void getRowVector03() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.getRowVector(-1); // You cant have negative columns
        });
    }

    @Test
    void getArray() {
        for (int i = 0; i < ROWS; i++) {
            assertArrayEquals(matrixArray[i], matrix.toArray()[i]);
        }
    }

    @RepeatedTest(ROWS)
    void setRowVector_Vector01(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        Vector vector = new DenseVector(new double[] {5,5,5});

        matrix = matrix.setRowVector(vector,rep);

        for (int i = 0; i < ROWS; i++) {
            if (i == rep) {
                assertEquals(vector, matrix.getRowVector(i));
            } else {
                assertArrayEquals(matrixArray[i], matrix.getRowVector(i).toArray());
            }
        }
    }

    @RepeatedTest(ROWS)
    void setRowVector_Vector02(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        Vector vector = new DenseVector(new double[] {5,5,5,5}); // Wrong sized vector. Real only have 3 values.

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix = matrix.setRowVector(vector,rep));
    }

    @RepeatedTest(ROWS)
    void setRowVector_Vector03(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        Vector vector = new DenseVector(new double[] {5,5}); // Wrong sized vector. Real have 3 values.

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix = matrix.setRowVector(vector,rep));
    }

    @RepeatedTest(ROWS)
    void setRowVector_Array01(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        double[] vector = {5,5,5};

        matrix = matrix.setRowVector(vector,rep);

        for (int i = 0; i < ROWS; i++) {
            if (i == rep) {
                assertArrayEquals(vector, matrix.getRowVector(i).toArray());
            } else {
                assertArrayEquals(matrixArray[i], matrix.getRowVector(i).toArray());
            }
        }
    }

    @RepeatedTest(ROWS)
    void setRowVector_Array02(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        double[] vector = {5,5,5,5};    // Wrong sized vector. Real only have 3 values.

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setRowVector(vector,rep));
    }

    @RepeatedTest(ROWS)
    void setRowVector_Array03(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        double[] vector = {5,5};    // Wrong sized vector. Real have 3 values.

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setRowVector(vector,rep));
    }

    @RepeatedTest(COLS)
    void setColumnVector_Vector01(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        Vector vector = new DenseVector(new double[] {5,5,5});

        matrix = matrix.setColumnVector(vector,rep);

        for (int i = 0; i < COLS; i++) {
            if (i == rep) {
                assertEquals(vector, matrix.getColumnVector(i));
            } else {
                // Assert
                assertArrayEquals(matrix.getColumnVector(i).toArray(), matrix.getColumnVector(i).toArray());
            }
        }
    }

    @RepeatedTest(ROWS)
    void setColumnVector_Vector02(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        Vector vector = new DenseVector(new double[] {5,5,5,5});    // Wrong sized vector. Real only have 3 values.

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumnVector(vector,rep));
    }

    @RepeatedTest(ROWS)
    void setColumnVector_Vector03(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        Vector vector = new DenseVector(new double[] {5,5});    // Wrong sized vector. Real have 3 values.

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumnVector(vector,rep));
    }

    @RepeatedTest(COLS)
    void setColumnVector_Array01(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        double[] vector = {5,5,5};

        matrix = matrix.setColumnVector(vector,rep);

        for (int i = 0; i < COLS; i++) {
            if (i == rep) {
                assertArrayEquals(vector, matrix.getColumnVector(i).toArray());
            } else {
                // Assert
                assertArrayEquals(matrix.getColumnVector(i).toArray(), matrix.getColumnVector(i).toArray());
            }
        }
    }

    @RepeatedTest(ROWS)
    void setColumnVector_Array02(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        double[] vector = {5,5,5,5};    // Wrong sized vector. Real only have 3 values.

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumnVector(vector,rep));
    }

    @RepeatedTest(ROWS)
    void setColumnVector_Array03(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition()-1;
        double[] vector = {5,5};    // Wrong sized vector. Real have 3 values.

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumnVector(vector,rep));
    }

    @Test
    void getEntryTest(){
        Matrix matrix = new MatrixBuilder()
                .addRow(2)
                .addRow(5,0,1)
                .addRow(0,1,5,0)
                .build();

        assertTrue(matrix.getEntry(0,0) == 2);
        assertTrue(matrix.getEntry(0,1) == 0);
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(-1,0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(3,0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(0,4));
    }


}