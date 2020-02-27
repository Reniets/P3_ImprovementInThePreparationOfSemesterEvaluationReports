package MachineLearning.NeuralNetwork.ANN;


import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.ActivationFunction;
import MachineLearning.NeuralNetwork.ANN.Layers.DenseLayer;
import MachineLearning.NeuralNetwork.ANN.Layers.Layer;
import MachineLearning.NeuralNetwork.ANN.Layers.LstmLayer;
import MachineLearning.NeuralNetwork.ANN.Layers.SoftmaxLayer;

public class ANNBuilder {

    // Fields:
    private int lastLayerSize;
    private ANN network;

    // Constructor:
    public ANNBuilder(int inputLayerSize) {
        this.lastLayerSize = inputLayerSize;
        this.network = new ANN(lastLayerSize);
    }

    // Methods:
    public ANNBuilder addDenseLayer(int outputSize, ActivationFunction activationFunction) {
        this.network.addLayer(new DenseLayer(this.lastLayerSize, outputSize, activationFunction));
        this.lastLayerSize = outputSize;
        return this;
    }

    public ANNBuilder addSoftmaxLayer(int outputSize) {
        if (this.lastLayerSize != outputSize) this.sizeMismatch("Softmax", this.lastLayerSize, outputSize);
        this.network.addLayer(new SoftmaxLayer(outputSize));

        return this;
    }

    public ANNBuilder addLstmLayer(int outputSize, int unrollLength) {
        this.network.addLayer(new LstmLayer(unrollLength, this.lastLayerSize, outputSize));
        this.lastLayerSize = outputSize;

        return this;
    }

    public ANNBuilder concatNetwork(ANN conNetwork) {
        if (this.lastLayerSize != conNetwork.getInputSize()) {
            this.sizeMismatch("concatNetwork", this.lastLayerSize, conNetwork.getInputSize());
        }

        for (Layer layer : conNetwork.getLayers()) {
            this.network.addLayer(layer);
        }

        this.lastLayerSize = conNetwork.getLayers().get(conNetwork.getLayers().size() - 1).getNodesInLayer();

        return this;
    }

    private void sizeMismatch(String name, int last, int next) {
        throw new IllegalArgumentException(name + ": Size mismatch: " + last + " vs. " + next);
    }

    public ANN build() {
        return this.network;
    }
}
