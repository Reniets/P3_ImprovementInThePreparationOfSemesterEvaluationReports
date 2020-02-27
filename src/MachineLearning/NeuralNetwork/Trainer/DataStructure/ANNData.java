package MachineLearning.NeuralNetwork.Trainer.DataStructure;

import LinearAlgebra.Types.Matrices.Matrix;

public interface ANNData extends Iterable<Matrix> {
    int getDataLength();

    int getDataEntries();

    Matrix getData(int index);

    boolean forceEvenSplits();
}
