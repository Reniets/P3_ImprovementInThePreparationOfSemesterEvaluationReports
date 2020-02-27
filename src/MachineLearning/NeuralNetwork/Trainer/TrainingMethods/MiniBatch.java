package MachineLearning.NeuralNetwork.Trainer.TrainingMethods;

import LinearAlgebra.Statics.Matrices;
import LinearAlgebra.Types.Matrices.Matrix;
import LinearAlgebra.Types.Matrices.MatrixBuilder;
import MachineLearning.NeuralNetwork.ANN.ANN;
import MachineLearning.NeuralNetwork.ANN.Layers.Layer;
import MachineLearning.NeuralNetwork.Trainer.Costs.CostFunction;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.ANNData;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.DefaultData;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.SequentialData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MiniBatch extends TrainingMethod {

    // Fields:
    private double learningRate;
    private int batchSize;

    // Constructor: 
    public MiniBatch(double learningRate, int batchSize, CostFunction CF) {
        super(CF);
        this.learningRate = learningRate;
        this.batchSize = batchSize;
    }

    @Override
    public double train(ANN network, ANNData allInputData, DefaultData allOutputData) {
        List<List<Matrix>> inputBatches = this.splitIntoBatches(allInputData, allInputData.forceEvenSplits());
        List<List<Matrix>> outputBatches = this.splitIntoBatches(allOutputData, allInputData.forceEvenSplits());

        int totalBatches = outputBatches.size();

        int randomSeed = new Random().nextInt();

        Collections.shuffle(inputBatches, new Random(randomSeed));
        Collections.shuffle(outputBatches, new Random(randomSeed));

        MatrixBuilder avgError = new MatrixBuilder(batchSize, allOutputData.getData(0).getColumns(), true);//new DenseMatrix(batchSize, allOutputData.getColumns());

        long total = 0;

        for (int i = 0; i < totalBatches; i++) {
            //System.out.print("Batch " + i + "/" + totalBatches + " : ");
            long start = System.currentTimeMillis();
            // Get batch
            ANNData inputs = this.convertToDataStructure(inputBatches.get(i));
            Matrix outputs = outputBatches.get(i).get(0);

            // Feed forward
            Matrix predictions = network.evaluateInputs(inputs);

            Matrix cost = CF.cost(outputs, predictions);
            avgError.additionToEntries(cost);

            Matrix outEffectMatrix = this.CF.costPrime(outputs, predictions);

            // Backpropagation
            int totalLayers = network.getLayers().size();
            for (int layerID = totalLayers - 1; layerID >= 0; layerID--) { // The "input layer" is not really a Layer, and therefor is not included in the list.
                Layer currentLayer = network.getLayers().get(layerID);  // Get current layer
                Matrix prevLayerOut = this.getOutFromLayer(network, layerID - 1, inputs.getData(inputs.getDataLength() - 1)); // Get the output matrix from the previous layer

                outEffectMatrix = currentLayer.backpropagate(prevLayerOut, outEffectMatrix);
            }

            this.updateWeights(network, learningRate);
            long end = System.currentTimeMillis();

            total += (end - start);
            //System.out.println((end - start) + " | " + (total / (i + 1)));
            network.clear();
        }

        avgError.setScalar(1 / (double) allOutputData.getData(0).getRows());

        Matrix error_matrix = avgError.build();

        //double sumValue = Matrices.sumRows(Matrices.sumColumns(avgError)).toArray()[0][0];
        double sumValue = Matrices.sumMatrix(error_matrix);
        //double totalValues = (error_matrix.getRows() * error_matrix.getColumns());

        return sumValue; // / totalValues;
    }

    private Matrix getOutFromLayer(ANN network, int layerID, Matrix inputMatrix) {
        if (layerID >= 0) {
            return network.getLayers().get(layerID).getOutMatrix();
        } else {
            return inputMatrix;
        }
    }

    private ANNData convertToDataStructure(List<Matrix> matrices) {
        if (matrices.size() == 1) {
            return new DefaultData(matrices.get(0));
        } else {
            return new SequentialData(matrices);
        }
    }

    private List<List<Matrix>> splitIntoBatches(ANNData data, boolean forceEven) {
        List<List<Matrix>> batches = new ArrayList<>();

        // Calculate total batches
        int totalBatches = data.getDataEntries() / this.batchSize;
        if (data.getDataEntries() % this.batchSize != 0) {
            totalBatches++;
        }

        // Prepare data structure
        for (int batch = 0; batch < totalBatches; batch++) {
            batches.add(new ArrayList<>());
        }

        // Split into batches
        for (int i = 0; i < data.getDataLength(); i++) {
            List<Matrix> dataBatches = this.splitMatrixInBatches(data.getData(i), totalBatches, forceEven);

            // Store batches corretly in the data structure
            for (int batch = 0; batch < totalBatches; batch++) {
                Matrix inputBatch = dataBatches.get(batch);

                batches.get(batch).add(inputBatch);
            }
        }

        return batches;
    }

    private List<Matrix> splitMatrixInBatches(Matrix matrix, int totalBatches, boolean forceEven) {
        ArrayList<Matrix> batches = new ArrayList<>(totalBatches);

        // Split into batches
        for (int i = 0; (i < totalBatches) && ((i + 1) * batchSize <= matrix.getRows()); i++) {
            final int rowStart = i * batchSize;
            final int rowsEnd = rowStart + batchSize;

            Matrix batch = Matrices.getPartialMatrix(matrix, rowStart, 0, rowsEnd, matrix.getColumns());
            batches.add(batch);
        }

        // Get any remaining parts
        if (batches.size() != totalBatches) {
            Matrix batch;

            if (forceEven) {
                MatrixBuilder matrixBuilder = new MatrixBuilder(this.batchSize, matrix.getColumns(), true);

                final int rowOffset = batches.size() * this.batchSize;

                for (int row = 0; row < this.batchSize; row++) {
                    for (int col = 0; col < matrix.getColumns(); col++) {
                        int matrixRow = (row + rowOffset) % matrix.getRows();

                        matrixBuilder.setEntry(row, col, matrix.getEntry(matrixRow, col));
                    }
                }

                batch = matrixBuilder.build();
            } else {
                batch = Matrices.getPartialMatrix(matrix, batches.size() * this.batchSize, 0, matrix.getRows(), matrix.getColumns());
            }

            batches.add(batch);
        }

        return batches;
    }

    // Updates the weights of the network for all the layers
    private void updateWeights(ANN network, double learningRate) {
        for (Layer layer : network.getLayers()) {
            layer.updateWeights(learningRate);
        }
    }

}
