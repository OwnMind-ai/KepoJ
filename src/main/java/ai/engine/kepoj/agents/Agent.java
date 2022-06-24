package ai.engine.kepoj.agents;


import ai.engine.kepoj.agents.deep.NeuralNetwork;

/**
 * Classes that implement the Agent interface can process data and save themselves to a file
 * @see NeuralNetwork
 * @since 1.1
 */
public interface Agent {
    /**
     * Processes data to some output
     * @since 1.1
     * @param data input data
     */
    double[] react(double[] data);   // Process environment state

    /**
     * @return length of output data
     * @since 1.2
     */
    int outputLength();
}
