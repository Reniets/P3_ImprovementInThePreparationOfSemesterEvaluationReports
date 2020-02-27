package LinearAlgebra.Statics;

import LinearAlgebra.Types.Vectors.DenseVector;
import LinearAlgebra.Types.Vectors.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VectorsTest_01 {

    // Test Fields:
    private static Vector vector;
    private static List<Vector> listOfVectors;

    // Test Constant:
    private static final int totalVectors = 2;

    @BeforeEach
    void beforeEach() {
        vector = new DenseVector(new double[]{1, 2, 3});

        listOfVectors = Arrays.asList(
                new DenseVector(new double[]{1, 0, 0}),
                new DenseVector(new double[]{0, 1, 0})
        );
    }

    // Tests
    // Check that the first two values are zero - The last can be anything.
    @Test
    void projectOrthogonal_NoLambda01() {
        Vector vectorOrthogonal = Vectors.projectOrthogonal(vector, listOfVectors);

        for (int i = 0; i < 2; i++) {
            assertEquals(0, vectorOrthogonal.getEntry(i));
        }
    }

    // Check that the vectors are orthogonal
    @RepeatedTest(totalVectors)
    void projectOrthogonal_NoLambda02(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition() - 1;

        Vector vectorOrthogonal = Vectors.projectOrthogonal(vector, listOfVectors);

        Vector currentVector = listOfVectors.get(rep);

        assertEquals(0, vectorOrthogonal.dotProduct(currentVector));
    }
}