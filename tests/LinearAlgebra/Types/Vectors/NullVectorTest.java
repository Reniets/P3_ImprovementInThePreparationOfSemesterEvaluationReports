package LinearAlgebra.Types.Vectors;

import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NullVectorTest {

    // Test Fields:
    private NullVector vector000;
    private NullVector vector001;
    private NullVector vector002;
    private Vector denseVector;
    private Matrix columnMatrix;

    @BeforeEach
    void beforeEach() {
        vector000 = new NullVector(3);
        vector001 = new NullVector(3);
        vector002 = new NullVector(2);
        denseVector = new VectorBuilder().addEntries(1, 2, 3).build();
        columnMatrix = new MatrixBuilder()
                .addColumn(1, 2, 3)
                .build();
    }

    // Tests:
    @Test
    void toArray() {
        double[] array = vector000.toArray();

        assertTrue(array.length == 3);

        for (int i = 0; i < array.length; i++) {
            assertTrue(array[i] == 0);
        }
    }

    @Test
    void dotProduct() {
        assertTrue(vector000.dotProduct(denseVector) == 0);
        assertThrows(IllegalArgumentException.class, () -> vector002.dotProduct(vector000));
    }

    @Test
    void mult() {
        assertTrue(vector000.equals(vector000.mult(10000)));
    }

    @Test
    void mult1() {
        Matrix matrix = new MatrixBuilder()
                .addColumn(0, 0, 0)
                .build();

        assertTrue(matrix.equals(vector000.mult(columnMatrix)));
        assertThrows(IllegalArgumentException.class, () -> vector002.mult(columnMatrix));
    }

    @Test
    void div() {
        assertTrue(vector000.equals(vector000.div(100)));
    }

    @Test
    void sub() {
        Vector negative = new VectorBuilder()
                .addEntries(-1, -2, -3)
                .build();
        assertTrue(negative.equals(vector000.sub(denseVector)));
        assertEquals(vector000, vector000.sub(vector001));
    }

    @Test
    void add() {
        assertTrue(denseVector.equals(vector000.add(denseVector)));
    }

    @Test
    void norm() {
        assertTrue(vector000.norm(100) == 0);
    }

    @Test
    void normMax() {
        assertTrue(vector000.normMax() == 0);
    }

    @Test
    void getEntry() {
        for (int i = 0; i < vector000.size(); i++) {
            assertTrue(vector000.getEntry(i) == 0);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> vector000.getEntry(3));
        assertThrows(IndexOutOfBoundsException.class, () -> vector000.getEntry(-1));
    }

    @Test
    void size() {
        assertTrue(vector000.size() == 3);
    }

    @Test
    void isNull() {
        assertTrue(vector000.isNull());
    }

    @Test
    void toRowMatrix() {
        Matrix matrix = new MatrixBuilder()
                .addRow(0, 0, 0)
                .build();

        assertEquals(matrix, vector000.toRowMatrix());
    }

    @Test
    void toColumnMatrix() {
        Matrix matrix = new MatrixBuilder()
                .addColumn(0, 0, 0)
                .build();

        assertEquals(matrix, vector000.toColumnMatrix());
    }

    @Test
    void isOnehot() {
        assertFalse(vector000.isOnehot());
    }

    @Test
    void isDense() {
        assertFalse(vector000.isDense());
    }

    @Test
    void cloneTest() {
        assertTrue(vector000.clone().equals(vector000));
        assertFalse(vector000.clone() == vector000);
    }
}