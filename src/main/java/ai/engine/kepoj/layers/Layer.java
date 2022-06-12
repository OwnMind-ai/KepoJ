package ai.engine.kepoj.layers;

import ai.engine.kepoj.entities.Neuron;

import java.io.Serializable;

public interface Layer extends Serializable {
    void buildLayer(double... data);

    void doLayer(double[] data);
    void trainLayer(double[] outputs, double ratio);
    void setOutputs(double[] outputs);
    void findErrors(double[] errors, double[][] weights);
    void setErrors(double[] errors);
    Neuron getNeuron(int index);

    double[][] getWeights();
    double[] getErrors();
    double[] getOutputs();

    @Deprecated
    double[] getBias();
    int length();
}
