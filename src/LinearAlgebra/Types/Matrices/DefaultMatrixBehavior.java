package LinearAlgebra.Types.Matrices;

import DataStructures.FunctionalInterfaces.TriConsumer;
import LinearAlgebra.Execution.Parallel;
import LinearAlgebra.Types.Vectors.DenseVector;
import LinearAlgebra.Types.Vectors.Vector;
import LinearAlgebra.Types.Vectors.VectorBuilder;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class DefaultMatrixBehavior implements Matrix {

    /*************
     *           *
     * Getters   *
     *           *
     ************/
    @Override
    public Vector getColumnVector(int column) {
        if (!(0 <= column && column <= this.getColumns())) {
            throw new IllegalArgumentException("There is no column number: " + column);
        }

        double[] columnVector = new double[this.getRows()];

        for (int row = 0; row < this.getRows(); row++) {
            double value = this.getEntry(row, column);
            columnVector[row] = value;
        }

        return new DenseVector(columnVector);
    }

    @Override
    public Vector getRowVector(int row) {
        if (!(0 <= row && row <= this.getRows())) {
            throw new IllegalArgumentException("There is no row number: " + row);
        }

        VectorBuilder vb = new VectorBuilder();

        for (int c = 0; c < getColumns(); c++) {
            vb.addEntries(getEntry(row, c));
        }

        return vb.build();
    }

    @Override
    public double[][] toArray() {
        int rows = this.getRows();
        int cols = this.getColumns();
        double[][] array = new double[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++)
                array[r][c] = this.getEntry(r, c);
        }

        return array;
    }

    @Override
    //Exports a byte sequence contain all numbers
    public List<Byte> getByteRepresentation() {
        // get rows and cols

        int matrixType = 0; //supposed to be different for oneohot (or potentially others)

        int rows = this.getRows();
        int cols = this.getColumns();

        //Add random but constant byte sequences to allow verifying the file format is correct.

        // Get byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((rows * cols) * 8 + 3 * 4);

        //Brand as type
        byteBuffer.putInt(matrixType);

        // Get bytes
        byteBuffer.putInt(rows);    // First add rows
        byteBuffer.putInt(cols);    // Then add cols

        // Then add all values
        this.compLoop((row, col, value) -> byteBuffer.putDouble(value));

        // Export
        ArrayList<Byte> out = new ArrayList<>();
        for (byte b : byteBuffer.array())
            out.add(b);

        return out;
    }

    @Override
    //Exports a byte sequence contain all numbers
    public void export(Path path) {
        List<Byte> bytes = this.getByteRepresentation();

        byte[] b = new byte[bytes.size()];
        for (int i = 0; i < b.length; i++)
            b[i] = bytes.get(i);

        try {
            Files.createDirectories(path.getParent());
            Files.write(path, b);
        } catch (Exception e) {
            throw new RuntimeException("Unable to write file at " + path);
        }
    }

    /*************
     *           *
     * Math OwO  *
     *           *
     *************/

    // Adds the values of a vector to all rows of a matrix. The original is unaltered
    public Matrix additionOfVectorToRows(Vector B) {
        if (this.getColumns() != B.size())
            throw new IllegalArgumentException("The size of the vector is not equal to the columns of the matrix");

        return new MatrixBuilder(this, true)
                .addVectorToRows(B)
                .build();
    }

    // Adds the values of a vector to all columns of a matrix. The original is unaltered
    public Matrix additionOfVectorToColumns(Vector B) {
        if (this.getRows() != B.size())
            throw new IllegalArgumentException("The size of the vector is not equal to the rows of the matrix");

        return new MatrixBuilder(this, true)
                .addVectorToColumns(B)
                .build();
    }

    // Returns a transposed version of the matrix. The original matrix is unaffected.
    public Matrix transpose() {
        /*
        return new MatrixBuilder(this, true)
                .buildTransposed();
                */
        final int rows = this.getRows();
        final int cols = this.getColumns();
        final int outRows = cols;
        final int outCols = rows;
        final double[][] out = new double[outRows][outCols];

        Parallel.forIteration(outRows, (r) -> {
            for (int c = 0; c < outCols; c++) {
                out[r][c] = this.getEntry(c, r);
            }
        });

        return new DenseMatrix(out);

    }

    @Override
    public Matrix transposeSoft() {
        return new TransposedMatrix(this);
    }

    public Matrix pow(int n) {
        return new MatrixBuilder(this, true)
                .setPow(n)
                .build();
    }


    @Override
    public Matrix clone() {
        return new MatrixBuilder(this, true).build();
    }

    /*************
     *           *
     * Setters   *
     *           *
     *************/


    // Sets a row vector, and returns a new matrix.
    @Override
    public Matrix setRowVector(Vector v, int n) {
        if (v.size() != this.getColumns()) {
            throw new IllegalArgumentException("The vector do not fit the matrix.");
        }

        boolean isStrict = n < this.getRows();

        return new MatrixBuilder(this, isStrict)
                .setRow(n, v)
                .build();
    }

    // Sets a row vector, and returns a new matrix.
    @Override
    public Matrix setRowVector(double[] v, int n) {
        if (v.length != this.getColumns()) {
            throw new IllegalArgumentException("The vector do not fit the matrix.");
        }

        return new MatrixBuilder(this, true)
                .setRow(n, v)
                .build();
    }

    // Sets a col vector, and returns a new matrix.
    @Override
    public Matrix setColumnVector(Vector v, int n) {
        if (v.size() != this.getRows()) {
            throw new IllegalArgumentException("The vector do not fit the matrix.");
        }

        return new MatrixBuilder(this, true)
                .setColumn(n, v)
                .build();
    }

    // Sets a col vector, and returns a new matrix.
    @Override
    public Matrix setColumnVector(double[] v, int n) {
        if (v.length != this.getRows()) {
            throw new IllegalArgumentException("The vector do not fit the matrix.");
        }

        return new MatrixBuilder(this, true)
                .setColumn(n, v)
                .build();
    }

    /********************
     *                  *
     * Other methods    *
     *                  *
     ********************/

    // Loop though every entry of the matrix, and runs the lambda expression
    public void compLoop(TriConsumer<Integer, Integer, Double> consumer) {
        final int cols = this.getColumns();
        final int rows = this.getRows();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                consumer.accept(row, col, this.getEntry(row, col));
            }
        }
    }

    /****************
     *              *
     * Operations   *
     *              *
     ****************/

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

        Parallel.forIteration(
                aRows,
                (Parallel.MINIMUM_MATRIX_ENTRIES_FOR_PARALLEL <= aRows * aCols || Parallel.MINIMUM_MATRIX_ENTRIES_FOR_PARALLEL <= bRows * bCols),
                i -> {
                    for (int a = 0; a < aCols; a++) {
                        double localityTemp = A.getEntry(i, a);
                        for (int b = 0; b < bCols; b++) {
                            endMatrix.additiontoEntry(i, b, localityTemp * B.getEntry(a, b));
                        }
                    }
                });

        return endMatrix.buildDenseMatrix();
    }

    // Multiplies matrix A with matrix B transposed (Cord flip), and returns the result. - A and B are unaltered.
    @SuppressWarnings("Duplicates")
    public Matrix multTrans(Matrix B) {
        Matrix A = this;
        return A.mult(B.transpose());
    }

    // Multiplies matrix A transposed (Cord flip) with matrix B transposed, and returns the result. - A and B are unaltered.
    @SuppressWarnings("Duplicates")
    public Matrix transMult(Matrix B) {
        Matrix A = this;
        return A.transpose().mult(B);
    }

    // Scales every entry in the matrix. - The original is unaffected.
    public Matrix mult(double scaler) {
        return new MatrixBuilder(this, true)
                .setScalar(scaler)
                .build();
    }

    // Multiplies matrix A with vector B, and returns the result. - A and B are unaltered.
    public Matrix mult(Vector v) {
        return this.mult(v.toColumnMatrix());
    }

    // Subtracts matrix B from matrix a, and returns the result. - A and B are unaltered.
    public Matrix sub(Matrix B) {
        Matrix A = this;

        if (A.getRows() != B.getRows() || A.getColumns() != B.getColumns()) {
            throw new IllegalArgumentException("Matrix A and B has to have the same size: " + A.getRows() + "-" + B.getRows() + " - " + A.getColumns() + "-" + B.getColumns());
        }

        return new MatrixBuilder(A, true)
                .subtractionToEntries(B)
                .build();
    }

    // Add matrix B to matrix A, and returns the result. - A and B are unaltered.
    public Matrix add(Matrix B) {
        Matrix A = this;

        if (A.getRows() != B.getRows() || A.getColumns() != B.getColumns()) {
            throw new IllegalArgumentException("Matrix A and B has to have the same size: [" + A.getRows() + "][" + A.getRows() + "] vs. [" + B.getColumns() + "][" + B.getColumns() + "]");
        }

        return new MatrixBuilder(A, true)
                .additionToEntries(B)
                .build();
    }


    // Component wise division - Divides every entry in A with the corresponding entry in B. - A and B are unaltered.
    @Override
    public Matrix compDivision(Matrix B) {
        Matrix A = this;

        if (A.getRows() != B.getRows() || A.getColumns() != B.getColumns()) {
            throw new IllegalArgumentException("Matrix A and B has to have the same size");
        }

        MatrixBuilder matrixBuilder = new MatrixBuilder(A.getRows(), A.getColumns(), true);

        this.compLoop((row, col, value) -> matrixBuilder.setEntry(row, col, A.getEntry(row, col) / B.getEntry(row, col)));

        return matrixBuilder.build();
    }

    // Component wise division - Divides every entry in A with the scalar entry scalar. - A are unaltered.
    @Override
    public Matrix compDivision(double scalar) {
        double multScalar = 1 / scalar;

        return this.mult(multScalar);
    }

    // Component wise multiplication - Multiplies every entry in A with the corresponding entry in B. - A and B are unaltered.
    @Override
    public Matrix compMult(Matrix B) {
        Matrix A = this;

        if (A.getRows() != B.getRows() || A.getColumns() != B.getColumns()) {
            throw new IllegalArgumentException("Matrix A[" + A.getRows() + "][" + A.getColumns() + "] and B[" + B.getRows() + "][" + B.getColumns() + "] has to have the same size");
        }

        MatrixBuilder matrixBuilder = new MatrixBuilder(A.getRows(), A.getColumns(), true);

        this.compLoop((row, col, value) -> matrixBuilder.setEntry(row, col, A.getEntry(row, col) * B.getEntry(row, col)));

        return matrixBuilder.build();

    }

    ///////////

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof Matrix)) return false;

        Matrix that = (Matrix) o;

        if (this.getRows() != that.getRows() ||
                this.getColumns() != that.getColumns()) {
            return false;
        }

        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getColumns(); c++) {
                if (this.getEntry(r, c) != that.getEntry(r, c))
                    return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getRows(), getColumns());
        result = 31 * result + Arrays.deepHashCode(toArray());
        return result;
    }

    @Override
    public String toString() {

        int[] colSpacing = new int[this.getColumns()];

        this.compLoop((r, c, v) -> colSpacing[c] = Math.max(colSpacing[c], String.valueOf(this.getEntry(r, c)).length()));

        StringBuilder sb = new StringBuilder()
                .append(this.getClass().getSimpleName())
                .append(":\n[[");

        final int rows = this.getRows();
        final int cols = this.getColumns();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double entry = this.getEntry(r, c);
                sb.append(entry);

                if (c + 1 < cols) {
                    sb.append(',');
                    sb.append(stringPadding(entry, colSpacing[c]));
                    sb.append(' ');
                }
            }
            if (r + 1 < rows) {
                sb.append("]\n [");
            } else {
                sb.append("]]");
            }
        }

        return sb.toString();
    }

    private String stringPadding(double entry, int max) {
        int spaces = max - String.valueOf(entry).length();
        StringBuilder sb = new StringBuilder();
        while (spaces-- > 0)
            sb.append(' ');

        return sb.toString();
    }
}
