package MachineLearning.NeuralNetwork.Trainer.DataStructure;

import LinearAlgebra.Types.Matrices.Matrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SequentialData implements ANNData {
    private List<Matrix> data = new ArrayList<>();
    private int dataEntriesPrMatrix = -1;

    public SequentialData() {

    }

    public SequentialData(List<Matrix> data) {
        this.data = data;

        this.dataEntriesPrMatrix = this.data.get(0).getRows();

        for (Matrix matrix : this.data) {
            if (matrix.getRows() != this.getDataEntries()) {
                throw new IllegalArgumentException("The sequential data HAS to have the same amount of rows pr. timestep.");
            }
        }
    }

    public void addSequentialData(Matrix matrix) {
        if (this.dataEntriesPrMatrix == -1) {
            this.dataEntriesPrMatrix = matrix.getRows();
        } else if (this.getDataEntries() != matrix.getRows()) {
            throw new IllegalArgumentException("The sequential data HAS to have the same amount of rows pr. timestep.");
        }

        this.data.add(matrix);
    }

    public void removeSequentialData(int index) {
        this.data.remove(index);

        if (this.data.size() == 0) {
            this.dataEntriesPrMatrix = -1;
        }
    }

    @Override
    public Iterator<Matrix> iterator() {
        return this.data.iterator();
    }

    @Override
    public int getDataLength() {
        return this.data.size();
    }

    @Override
    public int getDataEntries() {
        return this.dataEntriesPrMatrix;
    }

    @Override
    public Matrix getData(int index) {
        return this.data.get(index);
    }

    @Override
    public boolean forceEvenSplits() {
        return false;
    }
}
