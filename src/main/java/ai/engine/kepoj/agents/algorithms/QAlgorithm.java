package ai.engine.kepoj.agents.algorithms;

import ai.engine.kepoj.agents.Agent;

/**
 * Points to an agent that implements Q-learning algorithm
 *
 * @since 1.2
 * @see ai.engine.kepoj.agents.deep.QNeuralNetwork
 */
public interface QAlgorithm extends Agent {
    void learningIteration(double reward, double[] nextState, double discountFactor, double ratio);
}
