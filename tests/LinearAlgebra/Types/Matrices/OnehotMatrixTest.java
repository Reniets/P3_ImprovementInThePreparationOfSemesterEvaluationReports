package LinearAlgebra.Types.Matrices;

import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Vectors.OnehotVector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OnehotMatrixTest {

    // Tests:
    @Test
    void constructorTest() {
        assertThrows(IndexOutOfBoundsException.class, () -> new OnehotMatrix(10, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> new OnehotMatrix(10, 10));
    }

    @Test
    void toStringTest() {
        Matrix matrix = new MatrixBuilder()
                .addRow(1)
                .addRow(0, 0, 1)
                .addRow(0, 1, 0, 0)
                .build();

        String expected = "OnehotMatrix:\n" +
                "[[1.0, 0.0, 0.0, 0.0]\n" +
                " [0.0, 0.0, 1.0, 0.0]\n" +
                " [0.0, 1.0, 0.0, 0.0]]";

        assertEquals(matrix.toString(), expected);
    }

    // More rows than cols
    @Test
    void mult01() {
        Matrix onehot = new MatrixBuilder()
                .addRow(1)
                .addRow(0, 0, 1)
                .addRow(0, 1, 0, 0)
                .build();

        Matrix denseOnehot = new MatrixBuilder(onehot, true).buildDenseMatrix();

        Matrix randomDense = Matrices.randomMatrix(4, 2);

        assertEquals(denseOnehot.mult(randomDense), onehot.mult(randomDense));
    }

    // More cols than rows
    @Test
    void mult02() {
        Matrix onehot = new MatrixBuilder()
                .addRow(1)
                .addRow(0, 0, 1)
                .addRow(0, 1, 0, 0)
                .build();

        Matrix denseOnehot = new MatrixBuilder(onehot, true).buildDenseMatrix();

        Matrix randomDense = Matrices.randomMatrix(4, 6);

        assertEquals(denseOnehot.mult(randomDense), onehot.mult(randomDense));
    }

    // Square matrix
    @Test
    void mult03() {
        Matrix onehot = new MatrixBuilder()
                .addRow(1)
                .addRow(0, 0, 1)
                .addRow(0, 1, 0, 0)
                .build();

        Matrix denseOnehot = new MatrixBuilder(onehot, true).buildDenseMatrix();

        Matrix randomDense = Matrices.randomMatrix(4, 4);

        assertEquals(denseOnehot.mult(randomDense), onehot.mult(randomDense));
    }

    @Test
    void cloneTest() {
        Matrix matrix = new MatrixBuilder()
                .addRow(1)
                .addRow(0, 0, 1)
                .addRow(0, 1, 0, 0)
                .build();

        assertEquals(matrix, matrix.clone());
        assertFalse(matrix == matrix.clone());
    }

    @Test
    void getEntryTest() {
        Matrix matrix = new MatrixBuilder()
                .addRow(1)
                .addRow(0, 0, 1)
                .addRow(0, 1, 0, 0)
                .build();

        assertTrue(matrix.getEntry(0, 0) == 1);
        assertTrue(matrix.getEntry(0, 1) == 0);
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(3, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(0, 4));
    }

    @Test
    void multTest() {
        Matrix matrix000 = new MatrixBuilder()
                .addRow(3, 4, 3)
                .addRow(2, 4, 5)
                .addRow(1, 2, 0)
                .build();

        OnehotMatrix matrix001 = new OnehotMatrix(3, 0, 2, 0);

        Matrix matrix002 = matrix001.mult(matrix000);

        assertTrue(matrix002.getEntry(0, 0) == 3);
        assertTrue(matrix002.getEntry(0, 1) == 4);
        assertTrue(matrix002.getEntry(0, 2) == 3);

        assertTrue(matrix002.getEntry(1, 0) == 1);
        assertTrue(matrix002.getEntry(1, 1) == 2);
        assertTrue(matrix002.getEntry(1, 2) == 0);

        assertTrue(matrix002.getEntry(2, 0) == 3);
        assertTrue(matrix002.getEntry(2, 1) == 4);
        assertTrue(matrix002.getEntry(2, 2) == 3);

        assertThrows(IllegalArgumentException.class, () -> matrix001.mult(new OnehotMatrix(2,0,1,1,1)));

    }

    @Test
    void oneHotToDense() {
        Matrix onehot = new MatrixBuilder()
                .addRow(0, 0, 1, 0)
                .addRow(0, 1, 0, 0)
                .addRow(0, 0, 1, 0, 0)
                .build();

        Matrix dense = new MatrixBuilder(onehot, true)
                .build();

        assertTrue(dense.getColumns() == 5);
        assertTrue(dense.getRows() == 3);
    }

    @Test
    void arrayListOfOnehotAsConstructor(){
        Matrix onehot000 = new MatrixBuilder()
                .addRow(0, 0, 1, 0)
                .addRow(0, 1, 0, 0)
                .addRow(0, 0, 1, 0, 0)
                .build();

        ArrayList<OnehotVector> onehotVectors = new ArrayList<>();
        onehotVectors.add(new OnehotVector(2,4));
        onehotVectors.add(new OnehotVector(1,4));
        onehotVectors.add(new OnehotVector(2,5));

        Matrix onehot001 = new OnehotMatrix(onehotVectors);

        assertEquals(onehot000, onehot001);

    }

    /*

        public OnehotVector getRowAsOneHotVector(int row) {
        return new OnehotVector(oneIndexes[row], getColumns());
    }

    public int[] getOneIndexes() {
        return Arrays.copyOf(oneIndexes, oneIndexes.length);
    }

    public int getOneIndex(int row) {
        return oneIndexes[row];
    }
    */

    @Test
    void getRowAsOneHotVectorTest(){
        OnehotMatrix onehot000 = new OnehotMatrix(3,0,1,2);
        OnehotVector vector = new OnehotVector(1,3);
        assertEquals(vector,onehot000.getRowAsOneHotVector(1));
    }

    @Test
    void getOneIndexes(){
        OnehotMatrix onehot000 = new OnehotMatrix(3,0,1,2);
        int[] expected = new int[]{0,1,2};
        int[] actual = onehot000.getOneIndexes();
        assertTrue(expected[0] == actual[0]);
        assertTrue(expected[1] == actual[1]);
        assertTrue(expected[2] == actual[2]);
    }

    @Test
    void getOneIndex(){
        OnehotMatrix onehot000 = new OnehotMatrix(3,0,1,2);
        assertTrue(onehot000.getOneIndex(0) == 0);
        assertTrue(onehot000.getOneIndex(1) == 1);
        assertTrue(onehot000.getOneIndex(2) == 2);
    }


}
