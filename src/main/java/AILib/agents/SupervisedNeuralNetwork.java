package AILib.agents;

import AILib.entities.Dataset;
import AILib.layers.Layer;

import java.io.IOException;
import java.io.Serializable;

/**
 * Neural network class that trains by example's dataset
 * @see NeuralNetwork
 * @see Agent
 * @since 1.2
 */
public class SupervisedNeuralNetwork extends NeuralNetwork implements Serializable {
    /**
     * @param inputNeurons input layer length
     * @since 1.1
     */
    public SupervisedNeuralNetwork(int inputNeurons) {
        super(inputNeurons);
    }

    /**
     * Read serializable neural network from file
     * @param fileName path to file
     * @since 1.1
     */
    public SupervisedNeuralNetwork(String fileName) throws IOException, ClassNotFoundException {
        super(fileName);
    }

    /**
     * Calculates error of all neurons except an output layer
     * @since 1.1
     */
    private void findError(){
        for(int i = this.layers.size() - 2; i > 0; i--){
            this.layers.get(i).findErrors(
                    this.layers.get(i + 1).getErrors(),
                    this.layers.get(i + 1).getWeights()
            );
        }
    }

    /**
     * Updates neurons weights by backward passing error through layers
     * @param ratio fixed value of learning step reducing
     * @since 1.1
     */
    private void backWeights(double ratio) {
        for(int i = 1; i < this.layers.size(); i++)
            this.layers.get(i).trainLayer(this.layers.get(i - 1).getOutputs(), ratio);
    }

    /**
     * Calculates errors for last layer
     * @param dataset expected output
     * @since 1.1
     */
    private void datasetOffset(double[] dataset){
        Layer last = this.layers.get(this.layers.size() - 1);
        for(int i = 0; i < this.layers.get(this.layers.size() - 1).length(); i++)
            last.getNeuron(i).error = dataset[i] - last.getNeuron(i).output;
    }

    /**
     * Computes one training cycle
     * @param dataset unpacked dataset
     * @param ratio fixed value of learning step reducing
     * @return neural network error
     * @since 1.2
     */
    private double trainIteration(double[][][] dataset, double ratio){
        double loss = 0;

        for (double[][] data : dataset) {
            double[] result = this.react(data[0]);
            for (int a = 0; a < result.length; a++)
                loss += Math.pow((data[1][a] - result[a]), 2);

            this.datasetOffset(data[1]);
            this.findError();
            this.backWeights(ratio);
        }

        return loss;
    }

    /** Starts a loop in which it adjusts the weights of neurons until the error is less than permissible
     * @param dataset list of instructions for agent
     * @param ratio fixed value of learning step reducing
     * @param fault permissible error
     * @return final neural network loss
     * @since 1.2
     * @see SupervisedNeuralNetwork#train(Dataset, double, long)
     */
    public double train(Dataset dataset, double ratio, double fault){
        double loss = Double.POSITIVE_INFINITY;
        double[][][] unpackedDataset = dataset.toArray();

        for(long age = 0; loss >= fault; age++) {
           loss = this.trainIteration(unpackedDataset, ratio);
           System.out.println(age + " - " + loss);
        }

        return loss;
    }

    /**
     * Runs a loop in which it adjusts the weights of neurons for a certain number of generations
     * @param dataset list of instructions for agent
     * @param ratio fixed value of learning step reducing
     * @param ages ages count
     * @since 1.2
     * @return final neural network loss
     * @see SupervisedNeuralNetwork#train(Dataset, double, double)
     */
    public double train(Dataset dataset, double ratio, long ages){
        double loss = Double.POSITIVE_INFINITY;
        double[][][] unpackedDataset = dataset.toArray();

        for(long age = 0; age <= ages; age++) {
            loss = this.trainIteration(unpackedDataset, ratio);
            System.out.println(age + " - " + loss);
        }

        return loss;
    }
}
