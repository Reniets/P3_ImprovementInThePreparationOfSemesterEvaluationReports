package LinearAlgebra.Types;

import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Vectors.DenseVector;
import LinearAlgebra.Types.Vectors.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest_Math01 {

    // Test Fields:
    private static Vector vector;
    private static double[] vectorArray;

    // Test Constant:
    private static final int LENGTH = 3;

    @BeforeEach
    void beforeEach() {
        vectorArray = new double[]{1, 2, 3}; // DO NOT CHANGE - Some tests depend on these values.

        vector = new DenseVector(vectorArray);
    }

    // Tests:
    @Test
    void dotProduct01() {
        double expected = 14;

        assertEquals(expected, vector.dotProduct(vector), 0.00001);
    }

    @Test
    void dotProduct02() {
        Vector newVector = new DenseVector(4); // Other vector has size of 3 - And therefor incompatible

        Assertions.assertThrows(IllegalArgumentException.class, () -> vector.dotProduct(newVector));
    }

    @Test
    void dotProduct03() {
        Vector newVector = new DenseVector(2); // other vector has size of 3 - And therefor incompatible

        Assertions.assertThrows(IllegalArgumentException.class, () -> newVector.dotProduct(vector));
    }

    @Test
    void mult_Matrix01() {
        Matrix matrix = Matrices.getIdentityMatrix(3);
        Matrix expected = new DenseMatrix(new double[][]{vectorArray});

        assertEquals(expected, vector.mult(matrix));
    }

    @Test
    void mult_Matrix02() {
        Matrix matrix = Matrices.getIdentityMatrix(4); // The vector size is only 3 - Therefor incompatible.

        Assertions.assertThrows(IllegalArgumentException.class, () -> vector.mult(matrix));
    }

    @Test
    void mult_Scalar01() {
        double constant = 2;

        Vector newVector = vector.mult(constant);

        for (int i = 0; i < LENGTH; i++) {
            assertTrue(vectorArray[i] * constant == newVector.toArray()[i]);
        }
    }

    @Test
    void mult_Scalar02() {
        double constant = -2;

        Vector newVector = vector.mult(constant);

        for (int i = 0; i < LENGTH; i++) {
            assertTrue(vectorArray[i] * constant == newVector.toArray()[i]);
        }
    }

    @Test
    void mult_Scalar03() {
        double constant = 0.7;

        Vector newVector = vector.mult(constant);

        for (int i = 0; i < LENGTH; i++) {
            assertTrue(vectorArray[i] * constant == newVector.toArray()[i]);
        }
    }

    @Test
    void div01() {
        double constant = 2;

        Vector newVector = vector.div(constant);

        for (int i = 0; i < LENGTH; i++) {
            assertTrue(vectorArray[i] / constant == newVector.toArray()[i]);
        }
    }

    @Test
    void div02() {
        double constant = -2;

        Vector newVector = vector.div(constant);

        for (int i = 0; i < LENGTH; i++) {
            assertTrue(vectorArray[i] / constant == newVector.toArray()[i]);
        }
    }

    @Test
    void div03() {
        double constant = 0.7;

        Vector newVector = vector.div(constant);

        for (int i = 0; i < LENGTH; i++) {
            assertTrue(vectorArray[i] / constant == newVector.toArray()[i]);
        }
    }

    @Test
    void sub01() {
        Vector result = vector.sub(vector);

        assertEquals(new DenseVector(LENGTH), result);
    }

    @Test
    void sub02() {
        Vector subVector = new DenseVector(4); // Other vector has size 3, and therefor incompatible.

        Assertions.assertThrows(IllegalArgumentException.class, () -> vector.sub(subVector));
    }

    @Test
    void sub03() {
        Vector subVector = new DenseVector(2); // Other vector has size 3, and therefor incompatible.

        Assertions.assertThrows(IllegalArgumentException.class, () -> vector.sub(subVector));
    }

    @Test
    void norm01() {
        double expected = 6;
        assertEquals(expected, vector.norm(1));
    }

    @Test
    void norm02() {
        double expected = Math.sqrt(14);
        assertEquals(expected, vector.norm(2), 0.000001);
    }

    @Test
    void norm03() {
        double expected = Math.pow(6, (double) 2 / 3);
        assertEquals(expected, vector.norm(3), 0.000001);
    }

    @Test
    void norm04() {
        double expected = Math.pow(2, (double) 1 / 4) * Math.sqrt(7);
        assertEquals(expected, vector.norm(4), 0.000001);
    }

    @Test
    void normMax() {
        double expected = 3;
        assertEquals(expected, vector.normMax(), 0.000001);
    }

    @Test
    void normException01() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> vector.norm(0));
    }

    @Test
    void normException02() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> vector.norm(-1));
    }
}