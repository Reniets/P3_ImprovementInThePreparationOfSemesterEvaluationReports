package LinearAlgebra.Statics;

import LinearAlgebra.Types.Matrices.DenseMatrix;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatricesTestSimplify {

    // Test fields:
    private static Matrix funkyMatrix = new DenseMatrix(new double[][]{
            {1.3164489, 2.9164489, 3.5164489},
            {3.1164489, 4.3164489, 5.3164489},
            {6.7164489, 7.2164489, 8.9164489}
    });

    private static Matrix cleanMatrix = new DenseMatrix(new double[][]{
            {1, 3, 4},
            {3, 4, 5},
            {7, 7, 9}
    });

    private static Matrix roundedMatrix_2 = new DenseMatrix(new double[][]{
            {1.32, 2.92, 3.52},
            {3.12, 4.32, 5.32},
            {6.72, 7.22, 8.92}
    });

    private static Matrix roundedMatrix_3 = new DenseMatrix(new double[][]{
            {1.316, 2.916, 3.516},
            {3.116, 4.316, 5.316},
            {6.716, 7.216, 8.916}
    });

    // Tests
    @Test
    void simplify01() {
        assertEquals(cleanMatrix, Matrices.simplify(funkyMatrix, 0));
    }

    @Test
    void simplify02() {
        assertEquals(roundedMatrix_2, Matrices.simplify(funkyMatrix));
    }

    @Test
    void simplify03() {
        assertEquals(roundedMatrix_3, Matrices.simplify(funkyMatrix, 3));
    }

    // Test with non square matrix - More rows than cols
    @Test
    void simplify04() {
        Matrix randomMatrix = Matrices.randomMatrix(3, 2);
        Matrix simpleMatrix = Matrices.simplify(randomMatrix);

        assertEquals(randomMatrix.getRows(), simpleMatrix.getRows());
        assertEquals(randomMatrix.getColumns(), simpleMatrix.getColumns());
    }

    // Test with non square matrix - More cols than rows
    @Test
    void simplify05() {
        Matrix randomMatrix = Matrices.randomMatrix(2, 3);
        Matrix simpleMatrix = Matrices.simplify(randomMatrix);

        assertEquals(randomMatrix.getRows(), simpleMatrix.getRows());
        assertEquals(randomMatrix.getColumns(), simpleMatrix.getColumns());
    }


}