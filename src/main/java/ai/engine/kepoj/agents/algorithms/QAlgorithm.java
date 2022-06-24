package ai.engine.kepoj.agents.algorithms;

/**
 * Points to a class that implements Q-learning algorithm
 *
 * @since 1.2
 * @see ai.engine.kepoj.agents.deep.QNeuralNetwork
 */
public interface QAlgorithm{
    void learningIteration(double reward, double[] nextState, double discountFactor, double ratio);
}
