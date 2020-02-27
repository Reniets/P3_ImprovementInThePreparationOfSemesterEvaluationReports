package MachineLearning.NeuralNetwork.ANN;

import DataStructures.Bytes;
import LinearAlgebra.Types.Matrices.Matrix;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.ActivationFunction;
import MachineLearning.NeuralNetwork.ANN.Layers.DenseLayer;
import MachineLearning.NeuralNetwork.ANN.Layers.Layer;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.ANNData;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ANN {
    private ArrayList<Layer> layers = new ArrayList<>(2);
    private int inputSize;

    // Constructor for more advanced / customised networks
    public ANN(int inputSize) {
        this.inputSize = inputSize;
    }

    // Constructor used to create a full neural network at the initialization of the object.
    public ANN(ActivationFunction AF, int... layerSizes) {
        this(layerSizes[0]);    // Set input size

        for (int layer = 1; layer < layerSizes.length; layer++) {
            this.addLayer(layerSizes[layer], AF);
        }
    }

    // Constructor used to create a homogeneous network.
    public ANN(int inputSize, int outputSize, int hiddenLayers, int hiddenSize, ActivationFunction AF) {
        this(inputSize);    // Set input size

        // Add all hidden layers
        for (int layer = 0; layer < hiddenLayers; layer++) {
            this.addLayer(hiddenSize, AF);
        }

        // Add output layer
        this.addLayer(outputSize, AF);
    }

    // constructor used to import a neural network from a binary file.
    public ANN(Path networkPath) throws IOException {
        this.importANN(networkPath);
    }

    public ANN(byte[] bytes) {
        ANNFromByteRepresentation(ByteBuffer.wrap(bytes));
    }

    public ANN(ByteBuffer byteBuffer) {
        ANNFromByteRepresentation(byteBuffer);
    }

    ANN ANNFromByteRepresentation(ByteBuffer byteBuffer) {
        this.layers.clear();

        // Read input size
        this.inputSize = byteBuffer.getInt();

        while (byteBuffer.hasRemaining()) {
            this.layers.add(Layer.createLayerFromBytes(byteBuffer));
        }

        return this;
    }

    private ANN importANN(Path networkPath) throws IOException {
        byte[] byteArray = Files.readAllBytes(networkPath);
        return this.ANNFromByteRepresentation(ByteBuffer.wrap(byteArray));
    }

    public List<Byte> getByteRepresentation() {
        List<Byte> byteRepresentation = new ArrayList<>();

        byteRepresentation.addAll(Bytes.intToByteList(this.inputSize));

        for (Layer layer : layers) {
            byteRepresentation.addAll(layer.getByteRepresentation());
        }

        return byteRepresentation;
    }

    public void exportANN(Path networkPath) throws IOException {
        byte[] byteArray = Bytes.listToByteBuffer(this.getByteRepresentation()).array();

        Files.write(networkPath, byteArray);
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public int getInputSize() {
        return inputSize;
    }

    // Package proteced setter.
    void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public void clear() {
        for (Layer layer : layers) {
            layer.clear();
        }
    }

    public void addLayer(int nodes, ActivationFunction AF) {
        if (layers.size() == 0) {
            this.addLayer(new DenseLayer(inputSize, nodes, AF));

        } else {
            int nodesInPrevLayer = layers.get(layers.size() - 1).getNodesInLayer();
            this.addLayer(new DenseLayer(nodesInPrevLayer, nodes, AF));
        }
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
    }

    public Matrix evaluateInputs(ANNData inputs) {
        int totalLayers = layers.size();

        for (Matrix matrix : inputs) {
            layers.get(0).updateLayer(matrix);

            for (int i = 1; i < totalLayers; i++) {
                layers.get(i).updateLayer(layers.get(i - 1));
            }
        }

        return layers.get(totalLayers - 1).getOutMatrix();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ANN ann = (ANN) o;
        return inputSize == ann.inputSize &&
                Objects.equals(layers, ann.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layers, inputSize);
    }
}
