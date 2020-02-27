package MachineLearning.NeuralNetwork.Trainer;

import MachineLearning.NeuralNetwork.ANN.ANN;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.ANNData;
import MachineLearning.NeuralNetwork.Trainer.DataStructure.DefaultData;
import MachineLearning.NeuralNetwork.Trainer.TrainingMethods.TrainingMethod;

public class Trainer {

    // Fields:
    ANN network;
    TrainingMethod TM;

    // Constructor:
    public Trainer(ANN network, TrainingMethod TM) {
        this.network = network;
        this.TM = TM;
    }

    // Method to startTraining a neural network with backpropagation
    private void startTraining(ANN network, TrainingMethod TM, ANNData inputs, DefaultData outputs, int iterations) {

        /*
        this.checkDataSize(network, data);
        */

        network.clear();

        for (int i = 0; i < iterations; i++) {
            // Backpropagate over the whole data set.

            double error = TM.train(network, inputs, outputs);

            /*
            if (i == 0 || (i + 1) % 1 == 1) {
                System.out.println("[" + (i + 1) + "/" + iterations + "] " + error);
            }
            */

            //this.error = TM.getErrorRate();

            /*
            if (printResult){
                // Print the avg error
                this.printAvgError(this.error);

                // Print the current progress (1% at a time)
                this.printProgress(i, iterations);
            }
            */

        }
    }

    public void startTraining(ANNData inputs, DefaultData outputs, int iterations) {
        this.startTraining(this.network, this.TM, inputs, outputs, iterations);
    }
}
