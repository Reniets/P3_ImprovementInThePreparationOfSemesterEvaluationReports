package LinearAlgebra.Types.Matrices.InternalStorageTypes;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SparseDynamicMatrixStorage implements InternalMatrixStorage {

    // Fields:
    private Map<Pair<Integer, Integer>, Double> internalStorage = new HashMap<>();
    private int rows;
    private int cols;

    // Getters:
    public Set<Map.Entry<Pair<Integer, Integer>, Double>> getEntrySet() {
        return this.internalStorage.entrySet();
    }

    @Override
    public double getEntry(int row, int col) {
        this.verifyWithinBorders(row, col);

        Pair<Integer, Integer> key = new Pair<>(row, col);
        return this.internalStorage.getOrDefault(key, 0d);
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getCols() {
        return this.cols;
    }

    public int getTotalEntries() {
        return this.internalStorage.entrySet().size();
    }

    // Setter:
    @Override
    public void setEntry(int row, int col, double value) {
        this.rows = Math.max(this.rows, row + 1);
        this.cols = Math.max(this.cols, col + 1);

        this.internalStorage.put(new Pair<>(row, col), value);
    }

    // Methods:
    @Override
    public void additionToEntry(int row, int col, double value) {
        this.verifyWithinBorders(row, col);

        Pair<Integer, Integer> key = new Pair<>(row, col);
        double currentValue = this.internalStorage.get(key);
        internalStorage.put(key, currentValue + value);
    }

    @Override
    public void subtractionToEntry(int row, int col, double value) {
        this.verifyWithinBorders(row, col);

        Pair<Integer, Integer> key = new Pair<>(row, col);
        double currentValue = this.internalStorage.get(key);
        internalStorage.put(key, currentValue - value);
    }

    @Override
    public void multiplicationToEntry(int row, int col, double value) {
        this.verifyWithinBorders(row, col);

        Pair<Integer, Integer> key = new Pair<>(row, col);
        double currentValue = this.internalStorage.get(key);
        internalStorage.put(key, currentValue * value);
    }

    @Override
    public void divisionToEntry(int row, int col, double value) {
        this.verifyWithinBorders(row, col);

        Pair<Integer, Integer> key = new Pair<>(row, col);
        double currentValue = this.internalStorage.get(key);
        internalStorage.put(key, currentValue / value);
    }

    private void verifyWithinBorders(int row, int col) {
        if ((this.getRows() <= row) || (this.getCols() <= col)) {
            throw new IndexOutOfBoundsException();
        }
    }
}
