package LinearAlgebra.Types.Vectors;

import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OnehotVectorTest {

    // Test Fields:
    private OnehotVector vector000;
    private OnehotVector vector001;
    private OnehotVector vector002;
    private OnehotVector vector003;
    private DenseVector vectorDense;
    private DenseVector vectorDenseOneHot;

    @BeforeEach
    void beforeEach() {
        this.vector000 = new OnehotVector(1, 5);
        this.vector001 = new OnehotVector(1, 5);
        this.vector002 = new OnehotVector(4, 5);
        this.vector003 = new OnehotVector(3, 4);
        this.vectorDense = new VectorBuilder()
                .addEntries(0, 1, 2, 3, 4)
                .buildDenseVector();

        this.vectorDenseOneHot = new VectorBuilder()
                .addEntries(0, 1, 0, 0, 0)
                .buildDenseVector();
    }

    // Tests:
    @Test
    void InvalidConstructors() {
        assertThrows(IndexOutOfBoundsException.class, () -> new OnehotVector(10, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> new OnehotVector(-1, 10));
    }

    @Test
    void getArray() {
        double[] array = vector000.toArray();

        for (int i = 0; i < 5; i++) {
            assertTrue(array[i] == ((i == 1) ? 1 : 0));
        }

    }

    @Test
    void dotProduct() {
        assertTrue(vector000.dotProduct(vectorDense) == 1);
        assertTrue(vector002.dotProduct(vectorDense) == 4);

        assertThrows(IllegalArgumentException.class, () -> vector000.dotProduct(vector003));

    }

    @Test
    void mult() {
        Vector vector = vector000.mult(10);

        for (int i = 0; i < 5; i++) {
            assertTrue(vector.getEntry(i) == ((i == 1) ? 10 : 0));
        }
    }

    @Test
    void mult1() {

        Matrix matrix000 = new MatrixBuilder()
                .addRow(0, 1, 2)
                .addRow(3, 4, 5)
                .addRow(6, 7, 8)
                .addRow(9, 10, 11)
                .addRow(12, 13, 14)
                .build();

        Matrix matrix001 = vector000.mult(matrix000);
        assertTrue(matrix001.getRows() == 1);
        assertTrue(matrix001.getColumns() == 3);
        assertTrue(matrix001.getEntry(0, 0) == 3);
        assertTrue(matrix001.getEntry(0, 1) == 4);
        assertTrue(matrix001.getEntry(0, 2) == 5);

        assertThrows(IllegalArgumentException.class, () -> matrix000.mult(vector003));

    }

    @Test
    void div() {
        Vector vector = vector000.div(10);

        for (int i = 0; i < 5; i++) {
            assertTrue(vector.getEntry(i) == ((i == 1) ? 0.1 : 0));
        }
    }

    @Test
    void sub() {
        Vector vector = vector000.sub(vector002);

        assertTrue(vector.getEntry(0) == 0);
        assertTrue(vector.getEntry(1) == 1);
        assertTrue(vector.getEntry(2) == 0);
        assertTrue(vector.getEntry(3) == 0);
        assertTrue(vector.getEntry(4) == -1);

    }

    @Test
    void add() {
        Vector vector = vector000.add(vector000);

        for (int i = 0; i < 5; i++) {
            assertTrue(vector.getEntry(i) == ((i == 1) ? 2 : 0));
        }
    }

    @Test
    void norm() {
        assertTrue(vector000.norm(3093) == 1);
        assertThrows(IllegalArgumentException.class, () -> vector000.norm(0));
    }

    @Test
    void normMax() {
        assertTrue(vector000.normMax() == 1);
    }

    @Test
    void getEntry() {
        assertThrows(IndexOutOfBoundsException.class, () -> vector000.getEntry(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> vector000.getEntry(5));
    }

    @Test
    void length() {
        assertTrue(vector000.size() == 5);
    }

    @Test
    void isNullVector() {
        assertFalse(vector000.isNull());
    }

    @Test
    void toRowMatrix() {
        Matrix matrix = vector000.toRowMatrix();
        assertTrue(matrix.getRows() == 1);
        assertTrue(matrix.getColumns() == 5);

        for (int i = 0; i < 5; i++) {
            assertTrue(matrix.getEntry(0, i) == ((i == 1) ? 1 : 0));
        }
    }

    @Test
    void toColumnMatrix() {
        Matrix matrix = vector000.toColumnMatrix();
        assertTrue(matrix.getRows() == 5);
        assertTrue(matrix.getColumns() == 1);

        for (int i = 0; i < 5; i++) {
            assertTrue(matrix.getEntry(i, 0) == ((i == 1) ? 1 : 0));
        }
    }

    @Test
    void EqualsTest() {
        assertTrue(vector000.equals(vector001));
        assertFalse(vector000.equals(vector002));
        assertFalse(vector000.equals(vector003));
        assertFalse(vector000.equals(vectorDense));
        assertFalse(vector000.equals("string"));
        assertTrue(vector000.hashCode() == vector001.hashCode());

        assertTrue(vector000.equals(vectorDenseOneHot));
        assertTrue(vector000.hashCode() == vectorDenseOneHot.hashCode());
    }

    @Test
    void toStringTest() {
        assertEquals(vector000.toString(), "OnehotVector:[0.0, 1.0, 0.0, 0.0, 0.0]");
    }

    @Test
    void isDenseTest() {
        assertFalse(vector000.isDense());
    }

    @Test
    void cloneTesT() {
        Vector vector = vector000.clone();
        assertEquals(vector000, vector);
        assertFalse(vector000 == vector);
    }

    @Test
    void isGetEntryOutOfIndex() {
        Vector vector000 = new OnehotVector(1, 3);

        assertThrows(IndexOutOfBoundsException.class, () -> vector000.getEntry(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> vector000.getEntry(3));
    }

    @Test
    void getOneIndexTest(){
        OnehotVector o = new OnehotVector(1,4);
        assertTrue(o.getOneIndex() == 1);
    }
}