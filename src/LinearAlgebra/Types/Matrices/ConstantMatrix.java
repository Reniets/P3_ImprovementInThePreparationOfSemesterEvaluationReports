package LinearAlgebra.Types.Matrices;

import LinearAlgebra.Execution.Parallel;


public class ConstantMatrix extends DefaultMatrixBehavior {

    // Fields:
    private final double entry;
    private final int rows, cols;

    public ConstantMatrix(int rows, int cols, double entry) {
        this.entry = entry;
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
        return this.cols;
    }

    @Override
    public double getEntry(int row, int col) {
        return this.entry;
    }

    @Override
    public Matrix mult(Matrix B) {
        Matrix A = this;
        if (A.getColumns() != B.getRows()) {
            throw new IllegalArgumentException("The width of matrix A (" + A.getColumns() + ") is not equal to the height of matrix B (" + B.getRows() + ")");
        }

        final int aRows = A.getRows();
        final int aCols = A.getColumns();

        final int bRows = B.getRows();
        final int bCols = B.getColumns();

        MatrixBuilder endMatrix = new MatrixBuilder(A.getRows(), B.getColumns(), true);

        double[] row = new double[bCols];
        Parallel.forIteration(
                bCols,
                (Parallel.MINIMUM_MATRIX_ENTRIES_FOR_PARALLEL <= aRows * aCols || Parallel.MINIMUM_MATRIX_ENTRIES_FOR_PARALLEL <= bRows * bCols),
                b -> {
                    for (int i = 0; i < bRows; i++) {
                        row[b] += B.getEntry(i, b) * this.entry;
                    }
                });

        for (int a = 0; a < aRows; a++) {
            endMatrix.setRow(a, row);
        }

        return endMatrix.buildDenseMatrix();
    }

    @Override
    public Matrix compMult(Matrix B) {
        if (this.entry == 1) {
            Matrix A = this;

            if (A.getRows() != B.getRows() || A.getColumns() != B.getColumns()) {
                throw new IllegalArgumentException("Matrix A[" + A.getRows() + "][" + A.getColumns() + "] and B[" + B.getRows() + "][" + B.getColumns() + "] has to have the same size");
            }

            return B;
        } else {
            return super.compMult(B);
        }
    }
}
