package ai.engine.kepoj.agents.deep;

import ai.engine.kepoj.agents.Agent;
import ai.engine.kepoj.agents.algorithms.QAlgorithm;
import ai.engine.kepoj.utils.ArrayUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

 /**
 * Neural network class that implements Q-learning algorithms
 * @see NeuralNetwork
 * @see Agent
 * @since 1.2
 */
public class QNeuralNetwork extends NeuralNetwork implements QAlgorithm, Serializable {
    /**
     * Iteration counter. Self-increases after learning iteration work
     * @since 1.2
     * @see QNeuralNetwork#learningIteration(double, double[], double, double)
     */
    public long iteration = 1;

    /**
     * @param inputNeurons input layer length
     * @since 1.1
     */
    public QNeuralNetwork(int inputNeurons) {
        super(inputNeurons);
    }

    /**
     * Read serializable neural network from file
     * @param fileName path to file
     * @since 1.1
     */
    public QNeuralNetwork(String fileName) throws IOException, ClassNotFoundException {
        super(fileName);
    }

    /**
     * Updates neurons weights using reward and nextState with Q-learning algorithm.
     * Discount factor is raised to the power of QAgent.iteration. To off learning discounting, set discount factor at 1
     * @param reward agent reward for previous result
     * @param nextState next environment state
     * @param discountFactor decrease in learning rate at each iteration. Usually between 0 and 1
     * @param ratio fixed value of learning step reducing. Usually between 0 and 1
     * @since 1.1
     */
    public void learningIteration(double reward, double[] nextState, double discountFactor, double ratio){
        double[] nextQValues = this.react(nextState);
        this.setError(reward, nextQValues, discountFactor);

        this.findError();
        this.updateWeights(ratio);
        this.iteration++;
    }

    /**
     * Calculates and sets error to neuron with max Q-value
     * @param reward agent reward for previous result
     * @param nextQValues agent reaction
     * @param discountFactor decrease in learning rate at each iteration
     * @since 1.1
     */
    private void setError(double reward, double[] nextQValues, double discountFactor){
        double maxQ = Arrays.stream(nextQValues).summaryStatistics().getMax();
        int maxQIndex = ArrayUtils.getMaxIndex(nextQValues);
        this.layers.get(this.layers.size() - 1).getNeuron(maxQIndex).error =
                reward + maxQ * Math.pow(discountFactor, this.iteration) -
                this.layers.get(this.layers.size() - 1).getNeuron(maxQIndex).output;
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
    private void updateWeights(double ratio) {
        for(int i = 1; i < this.layers.size(); i++)
            this.layers.get(i).trainLayer(this.layers.get(i - 1).getOutputs(), ratio);
    }
}
