package LinearAlgebra.Types.Matrices;

import LinearAlgebra.Types.Vectors.OnehotVector;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnehotMatrix extends DefaultMatrixBehavior {

    // Fields:
    private int rows;
    private int columns;
    private int[] oneIndexes;

    // Constructors:
    public OnehotMatrix(int width, int... oneIndexes) {
        this.rows = oneIndexes.length;
        this.columns = width;
        this.oneIndexes = oneIndexes;

        for (int index : oneIndexes) {
            if (index < 0 || index >= width) {
                throw new IndexOutOfBoundsException("oneIndex list contains an index out of bounds.");
            }
        }
    }

    public OnehotMatrix(List<OnehotVector> vectors) {
        this.rows = vectors.size();
        this.columns = 0;
        this.oneIndexes = new int[this.rows];

        int r = 0;
        for (OnehotVector v : vectors) {
            this.columns = Math.max(v.size(), this.columns);
            this.oneIndexes[r++] = v.getOneIndex();
        }
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
        return this.columns;
    }

    @Override
    public double[][] toArray() {
        double[][] array = new double[this.rows][this.columns];

        for (int r = 0; r < this.rows; r++) {
            array[r][this.oneIndexes[r]] = 1;
        }

        return array;
    }

    @Override
    public double getEntry(int row, int col) {
        if (row < 0 || col < 0 || row >= getRows() || col >= getColumns())
            throw new IndexOutOfBoundsException();

        return (oneIndexes[row] == col) ? 1 : 0;
    }

    @Override
    public Matrix clone() {
        return new OnehotMatrix(this.columns, this.oneIndexes);
    }

    @Override
    //Exports a byte sequence only containing positions of ones
    public List<Byte> getByteRepresentation() {
        // get rows and cols

        int matrixType = 1; //supposed to be different for oneohot (or potentially others)

        int rows = this.getRows();
        int cols = this.getColumns();

        // Get byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((rows * 4) + 3 * 4);

        //Brand as type
        byteBuffer.putInt(matrixType);

        // Get bytes
        byteBuffer.putInt(rows);    // First add rows
        byteBuffer.putInt(cols);    // Then add cols

        // Then add all values
        for (int oneIndex : oneIndexes)
            byteBuffer.putInt(oneIndex);

        // Export
        ArrayList<Byte> out = new ArrayList<>();
        for (byte b : byteBuffer.array())
            out.add(b);

        return out;
    }

    public OnehotVector getRowAsOneHotVector(int row) {
        return new OnehotVector(this.oneIndexes[row], getColumns());
    }

    public int[] getOneIndexes() {
        return Arrays.copyOf(this.oneIndexes, this.oneIndexes.length);
    }

    public int getOneIndex(int row) {
        return this.oneIndexes[row];
    }

    /****************
     *              *
     * Mult         *
     *              *
     ****************/

    @Override
    public Matrix mult(Matrix B) {
        // Check the size of the matrices.
        Matrix A = this;

        if (A.getColumns() != B.getRows()) {
            throw new IllegalArgumentException("The width of matrix A (" + A.getColumns() + ") is not equal to the height of matrix B (" + B.getRows() + ")");
        }

        MatrixBuilder endMatrix = new MatrixBuilder(A.getRows(), B.getColumns(), true);

        for (int r = 0; r < this.getRows(); r++) {
            endMatrix.setRow(r, B.getRowVector(this.oneIndexes[r]));
        }

        return endMatrix.build();
    }

    // Multiplies matrix A with matrix B transposed (Cord flip), and returns the result. - A and B are unaltered.
    /*
    public Matrix multTrans(Matrix B) {
        Matrix A = this;
        //Re-try?
        // Check the size of the matrices.
        if (A.getColumns() != B.getColumns()) {
            throw new IllegalArgumentException("The width of matrix A is not equal to the height of matrix B");
        }

        MatrixBuilder endMatrix = new MatrixBuilder();

        int r = 0;
        for(int oneIndex: oneIndexes)
            endMatrix.setEntry(r++,oneIndex,B.getEntry(oneIndex,r));

        return endMatrix.build();
    }


    // Scales every entry in the matrix. - The original is unaffected.
    public Matrix mult(double scaler) {
        return new MatrixBuilder(this)
                .setScalar(scaler)
                .build();
    }

    // Multiplies matrix A with vector B, and returns the result. - A and B are unaltered.
    public Matrix mult(Vector v) {
        Matrix colVector = new MatrixBuilder()
                .addColumn(v)
                .build();

        return this.mult(colVector);
    }
    */
}
