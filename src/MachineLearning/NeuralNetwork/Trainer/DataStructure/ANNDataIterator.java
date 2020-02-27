package MachineLearning.NeuralNetwork.Trainer.DataStructure;

import LinearAlgebra.Types.Matrices.Matrix;

import java.util.Iterator;

public class ANNDataIterator implements Iterator<Matrix> {
    private Matrix data;
    private boolean readData = false;

    ANNDataIterator(Matrix data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        if (!this.readData) {
            this.readData = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Matrix next() {
        return data;
    }
}
