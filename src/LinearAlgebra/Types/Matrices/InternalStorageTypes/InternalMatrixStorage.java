package LinearAlgebra.Types.Matrices.InternalStorageTypes;

public interface InternalMatrixStorage {
    // Getters
    double getEntry(int row, int col);

    int getRows();

    int getCols();

    // Setter
    void setEntry(int row, int col, double value);

    // Basic operations
    void additionToEntry(int row, int col, double value);

    void subtractionToEntry(int row, int col, double value);

    void multiplicationToEntry(int row, int col, double value);

    void divisionToEntry(int row, int col, double value);
}
