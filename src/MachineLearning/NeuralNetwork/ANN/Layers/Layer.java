package MachineLearning.NeuralNetwork.ANN.Layers;

import LinearAlgebra.Types.Matrices.Matrix;

import java.nio.ByteBuffer;
import java.util.List;

public interface Layer {

    static Layer createLayerFromBytes(ByteBuffer byteBuffer) {                   // Creates a class specific layer from id and byte array
        int id = byteBuffer.getInt();
        switch (id) {
            case 1:
                return new DenseLayer().bytesToLayer(byteBuffer);
            case 2:
                return new LstmLayer().bytesToLayer(byteBuffer);
            case 3:
                return new SoftmaxLayer().bytesToLayer(byteBuffer);
            default:
                throw new IllegalArgumentException("No such layer ID: " + id);
        }
    }

    // Getters:
    Matrix getOutMatrix();                                              // Returns the output of the layer

    Matrix getNetMatrix();                                              // Returns the input of the layer

    int getNodesInLayer();                                              // Returns the amount of nodes in the layer (Equal to the length of the output vector)

    void clear();

    List<Byte> getByteRepresentation();                                 // Returns the byte representation of the layer

    Layer bytesToLayer(ByteBuffer byteBuffer);                          // Returns the layer created from the bytes

    void updateLayer(Matrix outMatrix);                                 // outMatrix: Output of last layer

    void updateLayer(Layer lastLayer);                                  // lastLayer: The previous layer

    Matrix backpropagate(Matrix prevOutMatrix, Matrix effectMatrix);    // prevOutMatrix: output of the previous layer - effectMatrix: effect Matrix of next layer - Return: Effect matrix of this layer

    void updateWeights(double learningRate);                            // Updates the weights dependent on the calculated derivatives and the given learning rate
}
