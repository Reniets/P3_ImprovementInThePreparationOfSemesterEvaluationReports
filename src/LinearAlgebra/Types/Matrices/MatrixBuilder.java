package LinearAlgebra.Types.Matrices;

import DataStructures.FunctionalInterfaces.TriConsumer;
import LinearAlgebra.Execution.Parallel;
import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.InternalStorageTypes.DynamicMatrixStorage;
import LinearAlgebra.Types.Matrices.InternalStorageTypes.InternalMatrixStorage;
import LinearAlgebra.Types.Matrices.InternalStorageTypes.SparseDynamicMatrixStorage;
import LinearAlgebra.Types.Matrices.InternalStorageTypes.StrictMatrixStorage;
import LinearAlgebra.Types.Vectors.OnehotVector;
import LinearAlgebra.Types.Vectors.Vector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * The matrix builder has no size, and will simply adjust it's size to accomedate any method calls.
 * This is why complex math is NOT possible! It belongs in the matrix class, which is slowly becoming immutable!
 * */

public class MatrixBuilder {

    // Fields:
    private InternalMatrixStorage internalStorage;
    private double scalar = 1;
    private int pow = 1;

    // Constant
    private static final Double NOTHING = 0d;

    /******************
     *                *
     * Constructors   *
     *                *
     ******************/

    public MatrixBuilder() {
        this.internalStorage = new DynamicMatrixStorage();
    }

    public MatrixBuilder(Matrix that, boolean isStrict) {
        int rows = that.getRows();
        int cols = that.getColumns();

        this.createInternalStorage(rows, cols, isStrict);

        that.compLoop((row, col, value) -> this.internalStorage.setEntry(row, col, value));
    }

    public MatrixBuilder(double[][] that, boolean isStrict) {
        int rows = that.length;
        int cols = that[0].length;

        this.createInternalStorage(rows, cols, isStrict);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double value = that[row][col];
                this.internalStorage.setEntry(row, col, value);
            }
        }
    }

    public MatrixBuilder(int rows, int cols, boolean isStrict) {
        this.createInternalStorage(rows, cols, isStrict);
    }

    private MatrixBuilder(InternalMatrixStorage storageType) {
        this.internalStorage = storageType;
    }

    private void createInternalStorage(int rows, int cols, boolean isStrict) {
        if (isStrict) {
            this.internalStorage = new StrictMatrixStorage(rows, cols);
        } else {
            this.internalStorage = new DynamicMatrixStorage(rows, cols);
        }
    }

    /******************
     *                *
     * Internal       *
     *                *
     ******************/

    private double calculateOutputEntry(double entry) {
        double val = entry;

        if (scalar == 0)
            return 0;

        if (scalar != 1)
            val *= scalar;

        return val;

    }

    /*********************
     *                   *
     * Internal: Getters *
     *                   *
     *********************/

    private int getColumns() {
        return this.internalStorage.getCols();
    }

    private int getRows() {
        return this.internalStorage.getRows();
    }

    private double getEntry(int row, int col) {
        return this.internalStorage.getEntry(row, col);
    }

    private ArrayList<Double> getRow(int rowIndex) {
        ArrayList<Double> row = new ArrayList<>();
        int cols = this.getColumns();

        for (int col = 0; col < cols; col++) {
            row.add(this.getEntry(rowIndex, col));
        }

        return row;
    }

    private ArrayList<Double> getColumn(int colIndex) {
        ArrayList<Double> column = new ArrayList<>();

        int height = getRows();
        for (int r = 0; r < height; r++) {
            column.add(this.getEntry(colIndex, r));
        }

        return column;
    }

    private int getNextColumnIndex() {
        return getColumns();
    }

    private int getNextRowIndex() {
        return Math.max(0, getRows());
    }

    /******************
     *                *
     * Misc           *
     *                *
     ******************/

    public MatrixBuilder setScalar(double scalar) {
        this.scalar = scalar;
        return this;
    }

    public MatrixBuilder setPow(int pow) {
        this.pow = pow;
        return this;
    }

    /******************
     *                *
     * Add Row        *
     *                *
     ******************/

    public MatrixBuilder addRow() {
        // Adds a full zero row at the end for the matrix
        int row = this.getNextRowIndex();
        int col = Math.max(0, this.getColumns() - 1);

        this.internalStorage.setEntry(row, col, NOTHING);
        return this;
    }

    public MatrixBuilder addRow(Vector v) {
        int rowIndex = this.getNextRowIndex();
        v.compLoop((i, val) -> this.setEntry(rowIndex, i, val));
        return this;
    }

    public MatrixBuilder addRow(double... entries) {
        int row = this.getNextRowIndex();
        for (int col = 0; col < entries.length; col++) {
            this.setEntry(row, col, entries[col]);
        }
        return this;
    }

    public MatrixBuilder addRow(Collection<Double> entries) {
        int row = this.getNextRowIndex();                            // Get the next entry
        Double[] entryArray = entries.toArray(new Double[0]);   // Convert collection to array

        // Add all entries to the matrix
        for (int col = 0; col < entries.size(); col++) {
            this.internalStorage.setEntry(row, col, entryArray[col]);
        }

        return this;
    }

    public MatrixBuilder addEmptyRows(int count) {
        for (int i = 0; i < count; i++)
            this.addRow();
        return this;
    }

    public MatrixBuilder addSizedRow(int size) {
        int row = this.getNextRowIndex();
        this.setEntry(row, size - 1, NOTHING);
        return this;
    }

    /******************
     *                *
     * Add Column     *
     *                *
     ******************/

    public MatrixBuilder addColumns(int count) {
        int row = Math.max(0, this.getRows() - 1);
        int col = this.getColumns() - 1;
        this.setEntry(row, col + count, NOTHING);
        return this;
    }

    public MatrixBuilder addColumn() {
        return this.addColumns(1);
    }

    public MatrixBuilder addColumn(double... entries) {
        int col = getColumns();
        for (int r = 0; r < entries.length; r++)
            this.setEntry(r, col, entries[r]);
        return this;
    }

    public MatrixBuilder addColumn(Vector v) {
        final int col = getNextColumnIndex();
        v.compLoop((i, val) -> this.setEntry(i, col, val));
        return this;
    }

    /******************
     *                *
     * Set Column     *
     *                *
     ******************/

    public MatrixBuilder setColumn(int column, ArrayList<Double> values) {
        double[] valuesArray = new double[values.size()];

        int i = 0;
        for (Double value : values)
            valuesArray[i++] = value;

        return setColumn(column, valuesArray);
    }

    public MatrixBuilder setColumn(int column, Vector vector) {
        vector.compLoop((i, v) -> this.setEntry(i, column, v));
        return this;
    }

    public MatrixBuilder setColumn(int column, double... entries) {
        //Make sure all rows are as wide as the matrix's widest point.
        for (int i = 0; i < entries.length; i++) {
            this.setEntry(i, column, entries[i]);
        }

        return this;
    }

    /******************
     *                *
     * isType         *
     *                *
     ******************/

    private boolean isOnehot() {
        if (scalar != 1 || this.getRows() == 0 || this.getColumns() == 0) {
            return false;
        }

        int rows = this.getRows();
        int cols = this.getColumns();

        for (int row = 0; row < rows; row++) {
            boolean metFirstOneInRow = false;

            for (int col = 0; col < cols; col++) {
                double value = this.getEntry(row, col);

                boolean valueIsOne = value == 1;

                if (valueIsOne && !metFirstOneInRow) {
                    // If the entry is one and it is the first one we have seen then remember it.
                    metFirstOneInRow = true;
                } else if ((valueIsOne && metFirstOneInRow)) {
                    // If we meet a second one, then return false
                    return false;
                } else if (value != 0) {
                    // If we meet any other number than 1 or zero then return false
                    return false;
                }
            }

            if (!metFirstOneInRow) {
                // If we never met a one, then it's not a onehot so return false
                return false;
            }
        }

        return true;
    }

    /******************
     *                *
     * Mathematical   *
     *                *
     ******************/


    public MatrixBuilder subtractionToEntry(int row, int col, double subtraction) {
        this.internalStorage.subtractionToEntry(row, col, subtraction);
        return this;
    }

    public MatrixBuilder additiontoEntry(int row, int col, double addition) {
        this.internalStorage.additionToEntry(row, col, addition);
        return this;
    }

    public MatrixBuilder additionToEntries(Matrix matrix) {
        if (this.internalStorage instanceof StrictMatrixStorage) {
            final int cols = matrix.getColumns();

            Parallel.forIteration(matrix.getRows(), (matrix.getRows() * cols > 100), (row) -> {
                for (int col = 0; col < cols; col++) {
                    this.additiontoEntry(row, col, matrix.getEntry(row, col));
                }
            });
        } else {
            matrix.compLoop(this::additiontoEntry);
        }

        return this;
    }

    public MatrixBuilder subtractionToEntries(Matrix matrix) {
        if (this.internalStorage instanceof StrictMatrixStorage) {
            final int cols = matrix.getColumns();

            Parallel.forIteration(matrix.getRows(), (row) -> {
                for (int col = 0; col < cols; col++) {
                    this.subtractionToEntry(row, col, matrix.getEntry(row, col));
                }
            });
        } else {
            matrix.compLoop(this::subtractionToEntry);
        }

        return this;
    }

    public MatrixBuilder additionToEntry(int row, int col, double val) {
        this.internalStorage.additionToEntry(row, col, val);
        return this;
    }

    public MatrixBuilder addVectorToRows(Vector B) {
        int rows = this.getRows();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < B.size(); c++) {
                additiontoEntry(r, c, B.getEntry(c));
            }
        }
        return this;
    }

    public MatrixBuilder addVectorToColumns(Vector B) {
        int rows = this.getRows();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < B.size(); c++) {
                additiontoEntry(r, c, B.getEntry(r));
            }
        }
        return this;
    }

    public MatrixBuilder multiplyRow(int rowIndex, double scalar) {

        int rowLength = this.getRows();
        for (int c = 0; c < rowLength; c++) {
            this.setEntry(rowIndex, c, getEntry(rowIndex, c) * scalar);
        }

        return this;
    }

    /************************
     *                      *
     *  Constant Entries    *
     *                      *
     ************************/

    public MatrixBuilder constantEntry(int rows, int cols, double constant) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.internalStorage.setEntry(row, col, constant);
            }
        }

        return this;
    }

    private MatrixBuilder fillIntoSquareMatrix() {
        int widest = getWidestDimension();

        if (this.getRows() != widest || this.getColumns() != widest) {
            this.setEntry(widest, widest, NOTHING);
        }

        return this;
    }

    /*********************
     *                   *
     *  Setters          *
     *                   *
     *********************/


    public MatrixBuilder setEntry(int row, int col, double val) {
        this.internalStorage.setEntry(row, col, val);
        return this;
    }

    public MatrixBuilder clearRow(int rowIndex) {
        int cols = this.getColumns();

        for (int col = 0; col < cols; col++) {
            this.setEntry(rowIndex, col, NOTHING);
        }

        return this;
    }

    public MatrixBuilder setRow(int rowIndex, ArrayList<Double> values) {

        double[] valuesArray = new double[values.size()];

        int i = 0;
        for (Double value : values)
            valuesArray[i++] = value;

        return setRow(rowIndex, valuesArray);
    }

    public MatrixBuilder setRow(int rowIndex, double... values) {
        int cols = values.length;

        for (int col = 0; col < cols; col++) {
            this.setEntry(rowIndex, col, values[col]);
        }

        return this;
    }

    public MatrixBuilder setRow(int rowIndex, Vector vector) {
        int cols = vector.size();

        for (int col = 0; col < cols; col++) {
            this.setEntry(rowIndex, col, vector.getEntry(col));
        }

        return this;
    }

    /*********************
     *                   *
     *  Swappers
     *                   *
     *********************/

    public MatrixBuilder swapRows(int row1, int row2) {
        ArrayList<Double> originalRow = this.getRow(row1);

        this.setRow(row1, this.getRow(row2));
        this.setRow(row2, originalRow);

        return this;
    }

    public MatrixBuilder swapColumns(int col1, int col2) {
        ArrayList<Double> originalCol = this.getColumn(col1);

        this.setColumn(col1, this.getColumn(col2));
        this.setColumn(col2, originalCol);

        return this;
    }

    private int getWidestDimension() {
        int columns = getColumns();
        int rows = getRows();
        return Math.max(columns, rows);
    }

    /******************
     *                *
     *  Builders      *
     *                *
     ******************/
    public Matrix build() {
        if (pow != 1) {
            if (pow == 0) {
                return Matrices.getIdentityMatrix(getWidestDimension());
            } else {
                fillIntoSquareMatrix();
                Matrix initial = this.makeDenseMatrix();
                Matrix out = initial;

                while (pow-- > 1)
                    out = out.mult(initial);

                return out;
            }
        }

        if (this.getRows() == 0 && this.getColumns() == 0) {
            return makeNullMatrix();
        } else if (isOnehot()) {
            return makeOneHotMatrix();
        } else {
            return makeDenseMatrix();
        }
    }

    public Matrix buildTransposed() {
        if (pow != 1) {
            if (pow == 0) {
                return Matrices.getIdentityMatrix(getWidestDimension());
            } else {
                fillIntoSquareMatrix();
                Matrix initial = this.makeDenseMatrixTransposed();
                Matrix out = initial;

                while (pow-- > 1)
                    out = out.mult(initial);

                return out;
            }
        } else
            return makeDenseMatrixTransposed();
    }

    public DenseMatrix buildDenseMatrix() {
        return makeDenseMatrix();
    }

    public static SparseMatrix buildSparseMatrix(List<Vector> vectorList) {
        MatrixBuilder builder = new MatrixBuilder(new SparseDynamicMatrixStorage());

        for (int row = 0; row < vectorList.size(); row++) {
            Vector vector = vectorList.get(row);

            for (int col = 0; col < vector.size(); col++) {
                double entry = vector.getEntry(col);

                if (entry != 0d) {
                    builder.setEntry(row, col, entry);
                }
            }
        }

        return new SparseMatrix((SparseDynamicMatrixStorage) builder.internalStorage);
    }

    public static Matrix buildEmpty(int size) {
        return buildEmpty(size, size);
    }

    public static Matrix buildEmpty(int rows, int colums) {
        return new MatrixBuilder(rows, colums, true)
                .build();

    }

    public static Matrix buildConstant(int rows, int colums, double constantEntry) {
        return new ConstantMatrix(rows, colums, constantEntry);
    }

    public static Matrix buildVectorList(List<Vector> vectorList) {

        if (vectorList.isEmpty()) {
            return buildEmpty(1, 1);
        }

        boolean isOneHot = true;
        int cols = -1;
        int rows = vectorList.size();

        for (Vector v : vectorList) {
            cols = Math.max(cols, v.size());
            if (isOneHot && !(v instanceof OnehotVector)) {
                isOneHot = false;
            }
        }

        if (isOneHot) { //This gets a warning, but it will never execute unless all entries are onehot!
            ArrayList<OnehotVector> onehotVectors = new ArrayList<>();
            for (Vector v : vectorList)
                onehotVectors.add((OnehotVector) v);

            return new OnehotMatrix(new ArrayList<>(onehotVectors));
        }

        MatrixBuilder mb = new MatrixBuilder(rows, cols, true);

        for (int r = 0; r < rows; r++) {
            mb.setRow(r, vectorList.get(r));
        }

        return mb.build();
    }


    /******************
     *                *
     *  Makers        *
     *                *
     ******************/

    private Matrix makeNullMatrix() {
        return new DenseMatrix(new double[][]{{0}});
    }

    private Matrix makeOneHotMatrix() {
        int rows = this.getRows();
        int cols = this.getColumns();
        int[] oneIndexes = new int[rows];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double value = this.getEntry(row, col);

                if (value == 1) {
                    oneIndexes[row] = col;
                    break;
                }
            }
        }

        return new OnehotMatrix(cols, oneIndexes);
    }

    @SuppressWarnings("Duplicates")
    private DenseMatrix makeDenseMatrix() {
        int height = Math.max(1, this.getRows());
        int width = Math.max(1, this.getColumns());
        double[][] entries = new double[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                double entry = getEntry(r, c);

                entries[r][c] = calculateOutputEntry(entry);
            }
        }

        return new DenseMatrix(entries);
    }

    @SuppressWarnings("Duplicates")
    private DenseMatrix makeDenseMatrixTransposed() {
        int height = this.getColumns();
        int width = this.getRows();
        double[][] entries = new double[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                double entry = getEntry(c, r);

                entries[r][c] = calculateOutputEntry(entry);
            }
        }

        return new DenseMatrix(
                entries
        );
    }

    /******************
     *                *
     *  compLoop      *
     *                *
     ******************/

    // Loop though every entry of the matrix, and runs the lambda expression
    public MatrixBuilder compLoop(TriConsumer<Integer, Integer, Double> consumer) {
        return compLoop(getRows(), getColumns(), consumer);
    }

    // Loop though every entry of the matrix, and runs the lambda expression
    public MatrixBuilder compLoop(int rows, int cols, TriConsumer<Integer, Integer, Double> consumer) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                consumer.accept(row, col, getEntry(row, col));
            }
        }
        return this;
    }

    public MatrixBuilder compDivision(double division) {
        return compMultiplication(1d / division);
    }

    public MatrixBuilder compMultiplication(double scalar) {
        return this.compLoop((r, c, v) -> setEntry(r, c, v * scalar));
    }

    /******************
     *                *
     *  Import        *
     *                *
     ******************/

    public static Matrix importMatrix(Path path) {
        try {
            byte[] byteArray = Files.readAllBytes(path);
            return importMatrix(byteArray);
        } catch (IOException e) {
            throw new RuntimeException("Could not load matrix at path " + path.toString());
        }
    }

    public static Matrix importMatrix(byte[] bytes) {
        return importMatrix(ByteBuffer.wrap(bytes));
    }

    public static Matrix importMatrix(ByteBuffer b) {
        int matrixType = b.getInt();
        switch (matrixType) {
            case 0:
                return importDense(b);
            case 1:
                return importOneHot(b);
            case 2:
                return importSparse(b);
            default:
                throw new RuntimeException("Unknown matrix type: " + matrixType);
        }
    }

    private static Matrix importSparse(ByteBuffer b) {
        int rows = b.getInt();
        int cols = b.getInt();
        int totalEntries = b.getInt();

        MatrixBuilder matrix = new MatrixBuilder(new SparseDynamicMatrixStorage());
        matrix.setEntry(rows - 1, cols - 1, 0); // Create the correct size

        for (int i = 0; i < totalEntries; i++) {
            int row = b.getInt();
            int col = b.getInt();

            double val = b.getDouble();

            matrix.setEntry(row, col, val);
        }

        return new SparseMatrix((SparseDynamicMatrixStorage) matrix.internalStorage);
    }

    private static Matrix importDense(ByteBuffer b) {
        int rows = b.getInt();
        int cols = b.getInt();
        int totalEntries = rows * cols;

        MatrixBuilder matrix = new MatrixBuilder(rows, cols, true);

        for (int i = 0; i < totalEntries; i++) {
            double val = b.getDouble();

            int row = i / cols;
            int col = i % cols;

            matrix.setEntry(row, col, val);
        }

        return matrix.buildDenseMatrix();
    }

    private static Matrix importOneHot(ByteBuffer b) {
        int rows = b.getInt();
        int cols = b.getInt();

        int[] oneEntries = new int[rows];

        for (int i = 0; i < rows; i++) {
            oneEntries[i] = b.getInt();
        }

        return new OnehotMatrix(cols, oneEntries);
    }

    /******************
     *                *
     * Strassan-Related
     *                *
     ******************/

    /******************
     *                *
     *  String        *
     *                *
     ******************/

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder()
                .append("MatrixBuilder:\n")
                .append('[');

        int rows = getRows();
        int cols = getColumns();
        for (int r = 0; r < rows; r++) {
            sb.append('[');
            for (int c = 0; c < cols; c++) {
                sb.append(getEntry(r, c));
                if (c < cols - 1) sb.append(", ");
            }
            if (r < rows - 1) sb.append("],\n ");
            else sb.append(']');
        }

        sb.append(']');

        return sb.toString();
    }

}
