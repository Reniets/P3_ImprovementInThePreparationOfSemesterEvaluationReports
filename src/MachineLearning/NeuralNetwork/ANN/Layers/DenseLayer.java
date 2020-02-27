package MachineLearning.NeuralNetwork.ANN.Layers;

import DataStructures.Bytes;
import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.ActivationFunction;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.ActivationFunctions;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.Enums.MatrixPref;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DenseLayer implements Layer {

    // Fields:
    private Matrix weightMatrix;        // The weight matrix
    private Matrix netMatrix;           // The net input matrix (Before activation)
    private Matrix outMatrix;           // The output matrix (After activation)
    private Matrix derivatives;         // The calculated partial derivatives of the weight matrix
    private Matrix biasWeightMatrix;    // The biases.
    private Matrix biasDerivatives;     // The calculated partial derivatives of the bias weight matrix

    private ActivationFunction AF;

    // Constructor
    // For all layers except the input layer, since the input layer wont actually be an actual layer.
    public DenseLayer(int nodesInPrevLayer, int nodesInThisLayer, ActivationFunction AF) {
        this.AF = AF;

        this.weightMatrix = Matrices.randomMatrix(nodesInPrevLayer, nodesInThisLayer, -1, 1);
        this.biasWeightMatrix = Matrices.randomMatrix(1, nodesInThisLayer, -1, 1);
    }

    // Package protected
    DenseLayer() {
        // Only used by import layer from bytes.
    }

    // Getters, access private fields:
    public Matrix getWeightMatrix() {
        return this.weightMatrix;
    }

    public void setWeightMatrix(Matrix weightMatrix) {
        this.weightMatrix = weightMatrix;
    }

    public Matrix getNetMatrix() {
        return this.netMatrix;
    }

    @Override
    public Matrix getOutMatrix() {
        return this.outMatrix;
    }

    public Matrix getDerivatives() {
        return this.derivatives;
    }

    public void setDerivatives(Matrix derivatives) {
        this.derivatives = derivatives;
    }

    public ActivationFunction getAF() {
        return this.AF;
    }

    public Matrix getBiasWeightMatrix() {
        return this.biasWeightMatrix;
    }

    // Setters:
    public void setBiasWeightMatrix(Matrix biasWeightMatrix) {
        this.biasWeightMatrix = biasWeightMatrix;
    }

    public Matrix getBiasDerivatives() {
        return this.biasDerivatives;
    }

    public void setBiasDerivatives(Matrix biasDerivatives) {
        this.biasDerivatives = biasDerivatives;
    }

    @Override
    public void updateLayer(Matrix outMatrix) {
        // Multiply output of last layer with the weight matrix
        this.netMatrix = outMatrix.mult(this.weightMatrix);

        // Add the bias
        this.netMatrix = this.netMatrix.additionOfVectorToRows(biasWeightMatrix.getRowVector(0));

        // Apply the activation function
        this.outMatrix = AF.activation(this.netMatrix);
    }

    @Override
    public void updateLayer(Layer prevLayer) {
        this.updateLayer(prevLayer.getOutMatrix());
    }

    @Override
    public Matrix backpropagate(Matrix prevOutMatrix, Matrix outEffectMatrix) {
        // Calculate the net effect from the out effect
        Matrix netEffectMatrix = this.calculateNetEffectMatrix(outEffectMatrix);

        // Calculate the average derivative matrices from both weights and bias
        Matrix weightDerivatives = this.calculateWeightDerivatives(prevOutMatrix, netEffectMatrix);
        Matrix biasDerivatives = this.calculateBiasDerivatives(netEffectMatrix);

        // Store the derivatives for layer weight update
        this.setDerivatives(weightDerivatives);
        this.setBiasDerivatives(biasDerivatives);

        // Calculate the outEffectMatrix for the previous layer and return it
        return netEffectMatrix.multTrans(this.getWeightMatrix());
    }

    private Matrix calculateNetEffectMatrix(Matrix outEffectMatrix) {
        // Get the activation prime matrix to make the activation jump from out to net effect.
        Matrix actiMatrix = this.AF.getMatrixPref() == MatrixPref.OUT ? this.getOutMatrix() : this.getNetMatrix();
        Matrix actiPrime = this.AF.activationPrime(actiMatrix);

        // multiply with the activation prime function to get the actual effect matrix.
        return outEffectMatrix.compMult(actiPrime);
    }

    // Calculate the average weight derivatives over the whole batch
    private Matrix calculateWeightDerivatives(Matrix prevOutMatrix, Matrix netEffectMatrix) {
        Matrix weightDerivatives = prevOutMatrix
                .transMult(netEffectMatrix)                // First get the summed derivatives over the whole batch.
                .compDivision(netEffectMatrix.getRows());  // Then get the average derivatives for the batch.

        return weightDerivatives;
    }

    // Calculate the average bias derivatives over the whole batch
    private Matrix calculateBiasDerivatives(Matrix netEffectMatrix) {
        Matrix biasDerivatives = Matrices
                .sumColumns(netEffectMatrix)               // First get the summed derivatives over the whole bach
                .compDivision(netEffectMatrix.getRows());  // Then get the average derivatives for the batch.

        return biasDerivatives;
    }

    @Override
    public void updateWeights(double learningRate) {
        // Multiply derivatives with the learning rate
        Matrix scaledWeightDerivatives = this.getDerivatives().mult(learningRate);
        Matrix scaledBiasDerivatives = this.getBiasDerivatives().mult(learningRate);

        // Calculate the new weights
        Matrix newWeights = this.getWeightMatrix().sub(scaledWeightDerivatives);
        Matrix newBiases = this.getBiasWeightMatrix().sub(scaledBiasDerivatives);

        // Set the weights
        this.setWeightMatrix(newWeights);
        this.setBiasWeightMatrix(newBiases);
    }

    @Override
    public int getNodesInLayer() {
        return this.weightMatrix.getColumns();
    }

    @Override
    public void clear() {
        // Do nothing
    }

    @Override
    public List<Byte> getByteRepresentation() {
        int layerType = 1;

        List<Byte> bytes = new ArrayList<>();
        bytes.addAll(Bytes.intToByteList(layerType));
        bytes.addAll(Bytes.intToByteList(this.AF.getID()));

        bytes.addAll(this.weightMatrix.getByteRepresentation());
        bytes.addAll(this.biasWeightMatrix.getByteRepresentation());

        return bytes;
    }

    @Override
    public Layer bytesToLayer(ByteBuffer byteBuffer) {
        this.AF = ActivationFunctions.getByID(byteBuffer.getInt());

        this.weightMatrix = MatrixBuilder.importMatrix(byteBuffer);
        this.biasWeightMatrix = MatrixBuilder.importMatrix(byteBuffer);

        return this;
    }

    // Handles equality
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DenseLayer layer = (DenseLayer) o;
        return Objects.equals(this.weightMatrix, layer.weightMatrix) &&
                Objects.equals(this.biasWeightMatrix, layer.biasWeightMatrix) &&
                Objects.equals(this.AF, layer.AF);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.weightMatrix, this.biasWeightMatrix, this.AF);
    }
}
