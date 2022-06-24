package ai.engine.kepoj.layers;

import ai.engine.kepoj.entities.Neuron;

import java.io.Serializable;

/**
 * Layer interface of a neural network. Each layer provides basic learning algorithms
 * even if the neural network isn't going to be trained.
 * @since 1.1
 */
public interface Layer extends Serializable {
    /**
     * Builds a layer structure based on the previous layer. Usually, Layer must be built in the neural network class.
     * @param previous layer object before current layer
     * @since 1.1
     */
    void buildLayer(Layer previous);

    /**
     * Processes data through every neuron
     * @param data input data with the same size as length of previous layer
     * @since 1.1
     */
    void doLayer(double[] data);

    /** Updates neurons weights based on their errors
     * @param outputs outputs values on the previous layer
     * @param ratio learning decreasing ratio
     * @since 1.1
     */
    void trainLayer(double[] outputs, double ratio);

    /**
     * Sets output values to the neurons
     * @since 1.1
     */
    void setOutputs(double[] outputs);

    /**
     * Calculates neuron errors based on the next layer's errors and weights between them
     * @param errors errors on the next layer
     * @param weights weights between current and next layers
     * @since 1.1
     */
    void findErrors(double[] errors, double[][] weights);

    Neuron getNeuron(int index);

    double[][] getWeights();
    double[] getErrors();
    double[] getOutputs();

    @Deprecated
    double[] getBias();

    /**
     * Returns count of the neurons on the layer
     * @return layer length
     * @since 1.1
     */
    int length();
}
