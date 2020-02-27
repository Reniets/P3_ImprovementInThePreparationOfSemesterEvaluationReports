package LinearAlgebra.Statics;

import LinearAlgebra.Types.Matrices.Matrix;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MatricesTest_Generators01 {

    // Test Field:
    private static Matrix randomMatrix;

    // Constants:
    private static final int ROWS = 2;
    private static final int COLS = 3;
    private static final int SEED = 123;
    private static final double MIN = 123;
    private static final double MAX = 125;

    @BeforeEach
    void beforeEach() {
        randomMatrix = Matrices.randomMatrix(ROWS, COLS, MIN, MAX, SEED);
    }

    // Assert that all values of a matrix are between min and max. Used multiple times to avoid duplicate code.
    void assertValuesBetweenMinAndMax(Matrix matrix, double min, double max) {
        // For every row
        for (double[] rowVector : matrix.toArray()) {
            // for every value
            for (double value : rowVector) {
                // Assert that the value is between min and max
                assertTrue(min <= value && value <= max);
            }
        }
    }

    // Tests:
    // Test correct amount of rows and cols
    @Test
    void randomMatrix01() {
        System.out.println(this.randomMatrix);
        System.out.println();

        assertEquals(this.ROWS, this.randomMatrix.getRows());
        assertEquals(this.COLS, this.randomMatrix.getColumns());
    }

    // Test correct min and max
    @Test
    void randomMatrix02() {
        assertValuesBetweenMinAndMax(randomMatrix, MIN, MAX);
    }

    // Test that the seed works
    @Test
    void randomMatrix03() {
        assertEquals(randomMatrix, Matrices.randomMatrix(ROWS, COLS, MIN, MAX, SEED));
    }

    // Test that an error is thrown if the max is lower than min
    @Test
    void randomMatrix04() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Matrices.randomMatrix(ROWS, COLS, 0, -1, SEED));
    }

    // Test that the random matrix with no arguments are between 0 and 1
    @Test
    void randomMatrix05() {
        randomMatrix = Matrices.randomMatrix(ROWS, COLS);

        assertValuesBetweenMinAndMax(randomMatrix, 0, 1);
    }

    // Test that the random matrix with no arguments are between 0 and 1 (+ seed)
    @Test
    void randomMatrix06() {
        randomMatrix = Matrices.randomMatrix(ROWS, COLS, SEED);

        assertValuesBetweenMinAndMax(randomMatrix, 0, 1);
    }

    // Test from 1->5 that an identity matrix is returned. If correct, rest is assumed correct too.
    @RepeatedTest(5)
    void getIdentityMatrix01(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition();

        Matrix identity = Matrices.getIdentityMatrix(rep);

        // For each value, if on the diagonal, then it has to be 1 otherwise 0.
        for (int row = 0; row < rep; row++) {
            for (int col = 0; col < rep; col++) {

                double expectedValue = (row == col) ? 1 : 0;
                assertEquals(expectedValue, identity.toArray()[row][col]);
            }
        }
    }

    // Test that the matrix has the correct size
    @RepeatedTest(5)
    void getIdentityMatrix02(RepetitionInfo repetitionInfo) {
        int rep = repetitionInfo.getCurrentRepetition();

        Matrix identity = Matrices.getIdentityMatrix(rep);

        assertEquals(rep, identity.getRows());
        assertEquals(rep, identity.getColumns());
    }
}