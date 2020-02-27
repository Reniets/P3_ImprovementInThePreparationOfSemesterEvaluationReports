package MachineLearning.NeuralNetwork.ANN.Layers;

import DataStructures.Bytes;
import DataStructures.Lists.CircularFifoQueue;
import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.ActivationFunction;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.SigmoidActivation;
import MachineLearning.NeuralNetwork.ANN.ActivactionFunctions.TanhActivation;
import MachineLearning.NeuralNetwork.ANN.Layers.Enum.LstmLayerNames;

import java.nio.ByteBuffer;
import java.util.*;

public class LstmLayer implements Layer {
    // Fields:
    private Map<LstmLayerNames, Layer> layers = new HashMap<>();
    private int outputNodes;
    private int inputSize;
    private int unrollLength;

    private Map<LstmLayerNames, CircularFifoQueue<Matrix>> outMatricies = new HashMap<>();
    private Map<LstmLayerNames, CircularFifoQueue<Matrix>> weightDerivatives = new HashMap<>();
    private Map<LstmLayerNames, CircularFifoQueue<Matrix>> biasDerivatives = new HashMap<>();

    private CircularFifoQueue<Matrix> lstmOutputs, lstmStates, concatInputs, tanhOuts;

    // Constants:
    private final static ActivationFunction TANH = new TanhActivation();
    private final static ActivationFunction SIGMOID = new SigmoidActivation();

    // Constructors:
    public LstmLayer(int unrollLength, int nodesInPreviousLayer, int nodesInThisLayer) {
        this.outputNodes = nodesInThisLayer;
        this.inputSize = nodesInPreviousLayer;
        this.unrollLength = unrollLength;

        int concatSize = this.outputNodes + this.inputSize;
        this.createLayers(concatSize);

        // Lists
        this.clear();
    }

    // Package protected
    LstmLayer() {
        // Only used by import layer from bytes.
    }

    // Methods:
    private void createLayers(int concatSize) {
        for (LstmLayerNames name : LstmLayerNames.values()) {
            ActivationFunction AF = (name.equals(LstmLayerNames.C)) ? new TanhActivation() : new SigmoidActivation();
            Layer layer = new DenseLayer(concatSize, this.outputNodes, AF);

            this.layers.put(name, layer);
        }
    }

    private void createLimitedListsMap(Map<LstmLayerNames, CircularFifoQueue<Matrix>> map, int limit) {
        for (LstmLayerNames name : LstmLayerNames.values()) {
            map.put(name, new CircularFifoQueue<>(limit));
        }
    }

    private void createLimitedList(int limit) {
        this.lstmOutputs = new CircularFifoQueue<>(limit);
        this.lstmStates = new CircularFifoQueue<>(limit);
        this.concatInputs = new CircularFifoQueue<>(limit);
        this.tanhOuts = new CircularFifoQueue<>(limit);
    }

    @Override
    public void updateLayer(Matrix outMatrix) {
        // Create the concat input
        Matrix prevOut = this.getPrevOut(outMatrix.getRows(), this.outputNodes);
        Matrix concatInput = Matrices.concatRows(prevOut, outMatrix);

        // Push the input though all layers
        for (Layer layer : layers.values()) {
            layer.updateLayer(concatInput);
        }

        // Calculate the LSTM state
        Matrix lstmState = this.getPrevState(outMatrix.getRows(), this.getNodesInLayer())
                .compMult(layers.get(LstmLayerNames.F).getOutMatrix())
                .add(
                        layers.get(LstmLayerNames.I).getOutMatrix()
                                .compMult(layers.get(LstmLayerNames.C).getOutMatrix())
                );

        // Calculate the tanh out
        Matrix tanhOut = TANH.activation(lstmState);

        // Calculate the output
        Matrix lstmOutput = tanhOut.compMult(layers.get(LstmLayerNames.O).getOutMatrix());

        // Store current timestep
        this.saveCurrentUpdate(lstmOutput, lstmState, concatInput, tanhOut);
    }

    @Override
    public void clear() {
        this.createLimitedListsMap(outMatricies, this.unrollLength);
        this.createLimitedListsMap(weightDerivatives, this.unrollLength);
        this.createLimitedListsMap(biasDerivatives, this.unrollLength);

        this.createLimitedList(this.unrollLength);
    }

    private Matrix getPrevOut(int rows, int cols) {
        Matrix prevOut = this.getOutMatrix();
        return (prevOut != null) ? prevOut : MatrixBuilder.buildEmpty(rows, cols);
    }

    private Matrix getPrevState(int rows, int cols) {
        Matrix prevState = lstmStates.getLast();
        return (prevState != null) ? prevState : MatrixBuilder.buildEmpty(rows, cols);
    }

    private void saveCurrentUpdate(Matrix lstmOutput, Matrix lstmState, Matrix concatInput, Matrix tanhOut) {
        for (LstmLayerNames name : LstmLayerNames.values()) {
            Layer layer = layers.get(name);
            outMatricies.get(name).add(layer.getOutMatrix());
        }

        this.lstmOutputs.add(lstmOutput);
        this.lstmStates.add(lstmState);
        this.concatInputs.add(concatInput);
        this.tanhOuts.add(tanhOut);
    }

    @Override
    public void updateLayer(Layer prevLayer) {
        this.updateLayer(prevLayer.getOutMatrix());
    }

    @Override
    public Matrix backpropagate(Matrix prevOutMatrix, Matrix effectMatrix) {
        // The prevOutMatrix is not important since this information will also be stored in the concat inputs

        // The prev state effect matrix is always zero at first unroll.
        Matrix prevStateEffectMatrix = MatrixBuilder.buildEmpty(effectMatrix.getRows(), effectMatrix.getColumns());

        // Backpropagate recursively though all unrolling steps, and return the effect matrix of the input vector of the previous unroll step.
        return this.backpropagateHelper(effectMatrix, prevStateEffectMatrix, this.lstmOutputs.size() - 1);
    }

    private Matrix backpropagateHelper(Matrix effectMatrix, Matrix prevStateEffectMatrix, int unrollStep) {
        // Calculate partial state effect
        Matrix state_Effect = effectMatrix
                .compMult(outMatricies.get(LstmLayerNames.O).get(unrollStep))
                .compMult(TANH.activationPrime(tanhOuts.get(unrollStep)))
                .add(prevStateEffectMatrix);

        // Calculate layer effects
        Matrix o_Effect = effectMatrix
                .compMult(tanhOuts.getLast())
                .compMult(SIGMOID.activationPrime(outMatricies.get(LstmLayerNames.O).get(unrollStep)));

        Matrix c_Effect = state_Effect
                .compMult(outMatricies.get(LstmLayerNames.I).get(unrollStep))
                .compMult(TANH.activationPrime(outMatricies.get(LstmLayerNames.C).get(unrollStep)));

        Matrix i_Effect = state_Effect
                .compMult(outMatricies.get(LstmLayerNames.C).get(unrollStep))
                .compMult(SIGMOID.activationPrime(outMatricies.get(LstmLayerNames.I).get(unrollStep)));

        Matrix f_Effect;
        if (unrollStep > 0) {
            f_Effect = state_Effect
                    .compMult(lstmStates.get(unrollStep - 1))
                    .compMult(SIGMOID.activationPrime(outMatricies.get(LstmLayerNames.F).get(unrollStep)));
        } else {
            f_Effect = MatrixBuilder.buildEmpty(lstmStates.getFirst().getRows(), lstmStates.getFirst().getColumns());
        }

        // Calculate full state effect
        state_Effect = state_Effect.compMult(outMatricies.get(LstmLayerNames.F).get(unrollStep));

        // Store effects in map for easier use
        Map<LstmLayerNames, Matrix> layerEffects = new HashMap<>();

        layerEffects.put(LstmLayerNames.F, f_Effect);
        layerEffects.put(LstmLayerNames.I, i_Effect);
        layerEffects.put(LstmLayerNames.C, c_Effect);
        layerEffects.put(LstmLayerNames.O, o_Effect);

        this.saveCurrentBackpropagate(layerEffects, unrollStep);

        if (unrollStep > 0) {
            // Calculate the netEffectMatrix for this unrolling step
            Matrix fico_part = getNetEffectMatrix(o_Effect, c_Effect, i_Effect, f_Effect);
            Matrix netEffectMatrix = Matrices.getPartialMatrix(fico_part, 0, 0, fico_part.getRows(), this.outputNodes);

            // Unroll the next step and backpropagate
            return backpropagateHelper(netEffectMatrix, state_Effect, unrollStep - 1);
        } else {
            // Calculate the netEffectMatrix for the next layer to use
            Matrix fico_part = getNetEffectMatrix(o_Effect, c_Effect, i_Effect, f_Effect);
            Matrix netEffectMatrix = Matrices.getPartialMatrix(fico_part, 0, this.outputNodes, fico_part.getRows(), fico_part.getColumns());

            return netEffectMatrix;
        }
    }

    private void saveCurrentBackpropagate(Map<LstmLayerNames, Matrix> layerEffects, int unrollStep) {
        // Calculate derivatives and store them
        for (LstmLayerNames name : LstmLayerNames.values()) {
            Matrix netEffectMatrix = layerEffects.get(name);

            Matrix weightDeri = concatInputs.get(unrollStep)
                    .transMult(netEffectMatrix)                // First get the summed derivatives over the whole batch.
                    .compDivision(netEffectMatrix.getRows());  // Then get the average derivatives for the batch.

            Matrix biasDeri = Matrices
                    .sumColumns(netEffectMatrix)               // First get the summed derivatives over the whole bach
                    .compDivision(netEffectMatrix.getRows());  // Then get the average derivatives for the batch.

            weightDerivatives.get(name).add(weightDeri);
            biasDerivatives.get(name).add(biasDeri);
        }
    }

    private Matrix getNetEffectMatrix(Matrix o_Effect, Matrix c_Effect, Matrix i_Effect, Matrix f_Effect) {
        Matrix f_part = f_Effect.multTrans(((DenseLayer) this.layers.get(LstmLayerNames.F)).getWeightMatrix());
        Matrix i_part = i_Effect.multTrans(((DenseLayer) this.layers.get(LstmLayerNames.I)).getWeightMatrix());
        Matrix c_part = c_Effect.multTrans(((DenseLayer) this.layers.get(LstmLayerNames.C)).getWeightMatrix());
        Matrix o_part = o_Effect.multTrans(((DenseLayer) this.layers.get(LstmLayerNames.O)).getWeightMatrix());

        return f_part.add(i_part).add(c_part).add(o_part);
    }

    @Override
    public void updateWeights(double learningRate) {
        for (LstmLayerNames name : LstmLayerNames.values()) {
            // Get the layer
            DenseLayer layer = (DenseLayer) layers.get(name); // Always dense since LSTM is implemented that way.

            // Calculate the average derivatives
            int size = this.weightDerivatives.get(name).size(); // Always the same size as bias
            Matrix weightDeriAvg = this.calculateDeriSum(this.weightDerivatives.get(name)).compDivision(size);
            Matrix biasDeriAvg = this.calculateDeriSum(this.biasDerivatives.get(name)).compDivision(size);

            // Store the derivatives
            layer.setDerivatives(weightDeriAvg);
            layer.setBiasDerivatives(biasDeriAvg);

            // Update the layers
            layer.updateWeights(learningRate);
        }
    }

    private Matrix calculateDeriSum(CircularFifoQueue<Matrix> list) {
        Matrix sumMatrix = list.getFirst();

        for (int i = 1; i < list.size(); i++) {
            sumMatrix = sumMatrix.add(list.get(i));
        }

        return sumMatrix;
    }

    @Override
    public Matrix getOutMatrix() {
        return this.lstmOutputs.getLast();
    }

    @Override
    public Matrix getNetMatrix() {
        Matrix prevConcat = this.concatInputs.getLast();
        return Matrices.getPartialMatrix(prevConcat, 0, this.inputSize - 1, prevConcat.getRows(), prevConcat.getColumns());
    }

    @Override
    public int getNodesInLayer() {
        return this.outputNodes;
    }

    @Override
    public List<Byte> getByteRepresentation() {
        int layerType = 2;

        List<Byte> bytes = new ArrayList<>();
        bytes.addAll(Bytes.intToByteList(layerType));
        bytes.addAll(Bytes.intToByteList(unrollLength));

        for (LstmLayerNames name : LstmLayerNames.values()) {
            bytes.addAll(layers.get(name).getByteRepresentation());
        }

        return bytes;
    }

    @Override
    public Layer bytesToLayer(ByteBuffer byteBuffer) {
        this.unrollLength = byteBuffer.getInt();
        this.clear();

        for (LstmLayerNames name : LstmLayerNames.values()) {
            layers.put(name, Layer.createLayerFromBytes(byteBuffer));
        }

        Layer f = layers.get(LstmLayerNames.F);
        this.outputNodes = f.getNodesInLayer();
        this.inputSize = ((DenseLayer) f).getWeightMatrix().getRows() - this.outputNodes;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LstmLayer lstmLayer = (LstmLayer) o;
        return outputNodes == lstmLayer.outputNodes &&
                inputSize == lstmLayer.inputSize &&
                unrollLength == lstmLayer.unrollLength &&
                layers.equals(lstmLayer.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layers, outputNodes, inputSize, unrollLength);
    }
}
