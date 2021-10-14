package AILib.layers;

import AILib.entities.Neuron;

public interface Layer {
    void buildLayer(int weightsCount);

    double[] doLayer(double[] data);
    void trainLayer(double[] outputs, float ratio);
    void setOutputs(double[] outputs);
    void findErrors(double[] errors, double[][] weights);
    void setErrors(double[] errors);
    Neuron getNeuron(int index);

    double[][] getWeights();
    double[] getErrors();
    double[] getOutputs();
    double[] getBias();
    int getNeuronsLength();
    double[] getArchivedData();
}
