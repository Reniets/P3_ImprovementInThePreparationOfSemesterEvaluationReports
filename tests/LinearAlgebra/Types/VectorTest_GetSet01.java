package LinearAlgebra.Types;

import LinearAlgebra.Types.Vectors.DenseVector;
import LinearAlgebra.Types.Vectors.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest_GetSet01 {

    // Test Fields:
    private static Vector vector;
    private static double[] vectorArray;

    // Test Constant:
    private static final int LENGTH = 3;

    @BeforeEach
    void beforeEach() {
        vectorArray = new double[]{1, 2, 3};

        vector = new DenseVector(vectorArray);
    }

    // Tests:
    @Test
    void getArray01() {
        assertArrayEquals(vectorArray, vector.toArray());
    }

    @Test
    void length01() {
        assertEquals(LENGTH, vector.size());
    }
}