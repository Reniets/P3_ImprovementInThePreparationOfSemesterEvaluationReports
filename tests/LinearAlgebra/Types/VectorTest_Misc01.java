package LinearAlgebra.Types;

import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import LinearAlgebra.Types.Vectors.DenseVector;
import LinearAlgebra.Types.Vectors.Vector;
import LinearAlgebra.Types.Vectors.VectorBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest_Misc01 {

    // Test Fields:
    private static Vector vector;
    private static double[] vectorArray;

    // Test Constants:
    private static final int LENGTH = 3;

    @BeforeEach
    void beforeEach() {
        vectorArray = new double[]{1, 2, 3}; // DO NOT CHANGE - Some tests depend on these values.

        vector = new VectorBuilder()
                .addEntries(vectorArray)
                .build();
    }

    // Tests:
    @Test
    void getValue() {
        for (int i = 1; i <= 3; i++) {
            assertEquals(i, vector.getEntry(i - 1));
        }
    }

    @Test
    void isNullVector01() {
        assertFalse(vector.isNull());
    }

    @Test
    void isNullVector02() {
        assertTrue(new DenseVector(3).isNull()); // Create vector of size 3. With all zero values.
    }

    @RepeatedTest(LENGTH)
    void isNullVector03(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition() - 1;
        vector.toArray()[rep] = 0;
        assertFalse(vector.isNull());
    }

    @Test
    void toRowMatrix() {
        Matrix expected = new DenseMatrix(new double[][]{vectorArray});
        assertEquals(expected, vector.toRowMatrix());
    }

    @Test
    void toColumnMatrix() {
        Matrix expected = new MatrixBuilder()
                .addRow(vectorArray)
                .buildTransposed();//new DenseMatrix(new double[][]{vectorArray}).transpose();

        assertEquals(expected, vector.toColumnMatrix());
    }

    @Test
    void toString01() {
        assertEquals("DenseVector:[1.0, 2.0, 3.0]", vector.toString());
    }

    // Check for equal content
    @Test
    void clone01() {
        assertEquals(vector, vector.clone());
    }

    // Check for different reference
    @Test
    void clone02() {
        assertTrue(vector != vector.clone());
    }

    // Check for same hashcode
    @Test
    void clone03() {
        assertEquals(vector.hashCode(), vector.clone().hashCode());
    }

    @Test
    void equalsTest_Symmetric01() {
        Vector x = vector;
        Vector y = vector.clone();

        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }

    @Test
    void equalsTest_Symmetric02() {
        Vector x = vector;
        Vector y = new VectorBuilder(x)
                .setEntry(0, -99)
                .build();

        assertFalse(x.equals(y) || y.equals(x));
        assertFalse(x.hashCode() == y.hashCode());
    }

    @Test
    void equalsTest_Symmetric03() {
        Vector y = vector.clone();

        double[] newVectorArray = new double[]{1, 2, 3};

        Vector newVector = new DenseVector(newVectorArray);

        assertTrue(newVector.equals(y) && y.equals(newVector));
        assertTrue(newVector.hashCode() == y.hashCode());
    }
}