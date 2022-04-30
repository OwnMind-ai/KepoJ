package AILib.agents;

import AILib.entities.Dataset;
import AILib.layers.Layer;

import java.io.IOException;
import java.io.Serializable;

/**
 * Neural network class that trains by example's dataset
 * @see NeuralNetwork
 * @see Agent
 * @since 1.1
 */
public class SupervisedAgent extends NeuralNetwork implements Serializable {
    /**
     * @param inputNeurons input layer length
     * @since 1.1
     */
    public SupervisedAgent(int inputNeurons) {
        super(inputNeurons);
    }

    /**
     * Read serializable neural network from file
     * @param fileName path to file
     * @since 1.1
     */
    public SupervisedAgent(String fileName) throws IOException, ClassNotFoundException {
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

    /** Starts a loop in which it adjusts the weights of neurons until the error is less than critical
     * @param dataset list of instructions for agent
     * @param ratio fixed value of learning step reducing
     * @since 1.1
     */
    public void train(double[][][] dataset, double ratio){
        double sumError = Double.POSITIVE_INFINITY;
        int age = 0;

        while (sumError >= this.fault) {
            sumError = 0;
            for (double[][] data : dataset) {
                double[] result = this.react(data[0]);
                for (int a = 0; a < result.length; a++)
                    sumError += Math.pow((data[1][a] - result[a]), 2);

                this.datasetOffset(data[1]);
                this.findError();
                this.backWeights(ratio);
            }

            System.out.println(age + " - " + sumError);
            age++;
        }
    }

    public void train(Dataset dataset, double ratio){ this.train(dataset.toArray(), ratio); }
}
