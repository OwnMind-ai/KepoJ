package AILib.agents;


/**
 * Classes that implement the Agent interface can process data and save themselves to a file
 * @see NeuralNetwork
 * @since 1.1
 */
public interface Agent {
    /**
     * Process data to some output
     * @since 1.1
     * @param data input data
     * @return processing result
     */
    double[] react(double[] data);   // Process environment state

    /**
     * @return length of output data
     * @since 1.2
     */
    int outputLength();
}
