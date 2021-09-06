package AILib.layers;

import AILib.utills.AIFunctions;
import AILib.utills.Neuron;

public interface Layer {
    void buildLayer(int weightsCount);

    double[] doLayer(double[] data);
    void trainLayer(double[] outputs, float ratio);
    void setOutputs(double[] outputs);
    void findErrors(double[] errors, double[][] weights);
    void setError(double[] errors);
    Neuron getNeuron(int index);

    double[][] getWeights();
    double[] getErrors();
    double[] getOutputs();
    double[] getBias();
    int getNeuronsLength();
    AIFunctions getAIFunction();
}