package LinearAlgebra.Types.Matrices;

import java.util.Arrays;

public class DenseMatrix extends DefaultMatrixBehavior {

    // Fields:
    public double[][] matrix;
    private int rows;
    private int columns;

    /******************
     *                *
     * Constructors   *
     *                *
     ******************/

    // Creates a matrix with predefined values.
    public DenseMatrix(double[][] matrix) {
        // Check that the matrix have an acceptable size.
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("A matrix cant be null nor have rows / columns of size 0");
        }

        // Save the matrix
        this.matrix = matrix;
        this.rows = matrix.length;
        this.columns = matrix[0].length;
    }

    // Creates an all zero matrix.
    public DenseMatrix(int rows, int columns) {
        this(new double[rows][columns]);
    }

    // Creates a matrix with all values equal to the constant provided.
    public DenseMatrix(int rows, int columns, double constant) {
        this(rows, columns);

        // If the constant is zero, no fill is required, as this is true by default.
        if (constant != 0) {
            for (double[] row : this.matrix) {
                Arrays.fill(row, constant);
            }
        }
    }

    /*************
     *           *
     * Getters   *
     *           *
     *************/

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public double[][] toArray() {
        int rows = this.getRows();
        int cols = this.getColumns();
        double[][] array = new double[rows][cols];

        for (int r = 0; r < rows; r++) {
            System.arraycopy(matrix[r], 0, array[r], 0, cols);
        }

        return array;
    }

    @Override
    public double getEntry(int row, int col) {
        return matrix[row][col];
    }

}
