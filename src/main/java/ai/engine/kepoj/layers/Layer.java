package ai.engine.kepoj.layers;

import ai.engine.kepoj.entities.Neuron;

import java.io.Serializable;

/**
 * Layer interface of a neural network. Each layer provides basic learning algorithms
 * even if the neural network isn't going to be trained.
 * @since 1.1
 */
public interface Layer extends Serializable {
    void buildLayer(double... data);

    void doLayer(double[] data);
    void trainLayer(double[] outputs, double ratio);
    void setOutputs(double[] outputs);
    void findErrors(double[] errors, double[][] weights);
    Neuron getNeuron(int index);

    double[][] getWeights();
    double[] getErrors();
    double[] getOutputs();

    @Deprecated
    double[] getBias();
    int length();
}
