package LinearAlgebra.Types.Decomposistions;

import DataStructures.FunctionalInterfaces.TriConsumer;
import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;

import java.util.function.BiConsumer;

public class QRDecomposition {

    // Fields:
    private MatrixBuilder rB;
    private MatrixBuilder qB;

    // Constructor:
    public QRDecomposition(Matrix A) {
        this.rB = new MatrixBuilder(Matrices.getIdentityMatrix(A.getColumns()), true);

        this.decompose(A);
    }

    // Getters;
    public Matrix getQ() {
        return this.qB.build();
    }

    public Matrix getR() {
        return this.rB.build();
    }

    // Orthogonalizes every >COLUMN< vector in the matrix, and creates two new matrices so that A = Q*R.
    private void decompose(Matrix A) {
        BiConsumer<Double, Integer> normHandler = (norm, i) -> rB.multiplyRow(i, norm);
        TriConsumer<Double, Integer, Integer> alphaHandler = (alpha, j, i) -> this.rB.setEntry(j, i, alpha);

        this.qB = new MatrixBuilder(Matrices.gramSchmidt(A, normHandler, alphaHandler), true);
    }
}
