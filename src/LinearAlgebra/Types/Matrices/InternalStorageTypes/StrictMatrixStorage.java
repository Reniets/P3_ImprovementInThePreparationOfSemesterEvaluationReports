package LinearAlgebra.Types.Matrices.InternalStorageTypes;

public class StrictMatrixStorage implements InternalMatrixStorage {

    // Fields:
    private double[][] internalStorage;
    private int rows;
    private int cols;

    // Constructor:
    public StrictMatrixStorage(int strictRows, int strictCols) {
        this.internalStorage = new double[strictRows][strictCols];
        this.rows = strictRows;
        this.cols = strictCols;
    }

    // Getters:
    @Override
    public double getEntry(int row, int col) {
        return this.internalStorage[row][col];
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getCols() {
        return this.cols;
    }

    // Setter:
    @Override
    public void setEntry(int row, int col, double value) {
        this.internalStorage[row][col] = value;
    }

    // Methods:
    @Override
    public void additionToEntry(int row, int col, double value) {
        this.internalStorage[row][col] += value;
    }

    @Override
    public void subtractionToEntry(int row, int col, double value) {
        this.internalStorage[row][col] -= value;
    }

    @Override
    public void multiplicationToEntry(int row, int col, double value) {
        this.internalStorage[row][col] *= value;
    }

    @Override
    public void divisionToEntry(int row, int col, double value) {
        this.internalStorage[row][col] /= value;
    }
}
