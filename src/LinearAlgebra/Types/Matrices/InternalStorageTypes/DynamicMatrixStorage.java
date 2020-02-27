package LinearAlgebra.Types.Matrices.InternalStorageTypes;

import java.util.ArrayList;
import java.util.List;

public class DynamicMatrixStorage implements InternalMatrixStorage {

    // Field:
    private List<List<Double>> internalStorage;

    // Constant:
    private final static double NOTHING = 0d;

    // Constructors:
    public DynamicMatrixStorage(int initialRows, int initialCols) {
        // Initialize rows
        this.internalStorage = new ArrayList<>(initialRows);

        // Initialize cols
        for (int row = 0; row < initialRows; row++) {
            this.internalStorage.add(new ArrayList<>(initialCols));
        }

        // Initialize values
        this.fillEmptyWithNothing(initialCols);
    }

    public DynamicMatrixStorage() {
        // Initialize internal storage
        this.internalStorage = new ArrayList<>();
    }

    // Getters:
    @Override
    public double getEntry(int row, int col) {
        try {
            return this.internalStorage.get(row).get(col);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    @Override
    public int getRows() {
        return this.internalStorage.size();
    }

    @Override
    public int getCols() {
        try {
            return this.internalStorage.get(0).size();
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    // Setter:
    @Override
    public void setEntry(int row, int col, double value) {
        guaranteeEntryAllocation(row + 1, col + 1);
        this.internalStorage.get(row).set(col, value);
    }

    private void guaranteeEntryAllocation(int rows, int cols) {

        if (getRows() >= rows && getCols() >= cols)
            return;

        int maxCols = Math.max(this.getCols(), cols);

        // Made sure that there are the correct amount of rows
        while (this.internalStorage.size() < rows) {
            this.internalStorage.add(new ArrayList<>());
        }

        // Make sure that there atleast is one row so adding a column does not fuck it up
        this.guaranteeOneRow();

        // Fill the values with zeros
        this.fillEmptyWithNothing(maxCols);
    }

    private void fillEmptyWithNothing(int cols) {
        for (List<Double> row : this.internalStorage) {
            while (row.size() < cols) {
                row.add(NOTHING);
            }
        }
    }

    private void guaranteeOneRow() {
        if (this.internalStorage.isEmpty())
            this.internalStorage.add(new ArrayList<>());
    }

    @Override
    public void additionToEntry(int row, int col, double value) {
        this.setEntry(row, col, this.getEntry(row, col) + value);
    }

    @Override
    public void subtractionToEntry(int row, int col, double value) {
        this.setEntry(row, col, this.getEntry(row, col) - value);
    }

    @Override
    public void multiplicationToEntry(int row, int col, double value) {
        this.setEntry(row, col, this.getEntry(row, col) * value);
    }

    @Override
    public void divisionToEntry(int row, int col, double value) {
        this.setEntry(row, col, this.getEntry(row, col) / value);
    }
}
