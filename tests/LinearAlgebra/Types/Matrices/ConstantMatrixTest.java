package LinearAlgebra.Types.Matrices;

import LinearAlgebra.Statics.Matrices;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantMatrixTest {

    @Test
    void getEntry() {
        Matrix matrix = new ConstantMatrix(10, 10, 2);

        assertEquals(2, matrix.getEntry(0, 0));
    }

    @Test
    void mult01() {
        Matrix matrix = new ConstantMatrix(2, 2, 2);

        Matrix result = matrix.mult(Matrices.getIdentityMatrix(2));

        Matrix expected = new MatrixBuilder(2, 2, true)
                .setRow(0, 2, 2)
                .setRow(1, 2, 2)
                .build();

        assertEquals(expected, result);
    }

    @Test
    void mult02() {
        MatrixBuilder builder = new MatrixBuilder(50,50,true);

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                builder.setEntry(i, j, 2);
            }
        }

        Matrix dense = builder.buildDenseMatrix();
        Matrix constant = new ConstantMatrix(50, 50, 2);

        assertEquals(dense, constant);

        Matrix random = Matrices.randomMatrix(50, 50);

        assertEquals(dense.mult(random), constant.mult(random));
    }

    @Test
    void mult03() {
        MatrixBuilder builder = new MatrixBuilder(50,50,true);

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                builder.setEntry(i, j, 1);
            }
        }

        Matrix dense = builder.buildDenseMatrix();
        Matrix constant = new ConstantMatrix(50, 50, 1);

        assertEquals(dense, constant);

        Matrix random = Matrices.randomMatrix(50, 50);

        assertEquals(dense.mult(random), constant.mult(random));
    }
}