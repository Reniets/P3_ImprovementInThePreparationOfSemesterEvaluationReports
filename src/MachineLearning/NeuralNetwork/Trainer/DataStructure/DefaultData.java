package MachineLearning.NeuralNetwork.Trainer.DataStructure;

import LinearAlgebra.Types.Matrices.Matrix;

import java.util.Iterator;

public class DefaultData implements ANNData {
    private Matrix data;

    public DefaultData(Matrix data) {
        this.data = data;
    }

    @Override
    public Iterator<Matrix> iterator() {
        return new ANNDataIterator(this.data);
    }

    @Override
    public int getDataLength() {
        if (this.data == null) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getDataEntries() {
        return data.getRows();
    }

    @Override
    public Matrix getData(int index) {
        if (index == 0) {
            return this.data;
        } else {
            throw new IndexOutOfBoundsException(index);
        }
    }

    @Override
    public boolean forceEvenSplits() {
        return false;
    }
}
