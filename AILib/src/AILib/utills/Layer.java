package AILib.utills;

public interface Layer {
    void buildLayer(int weightsCount);

    void doLayer(double[] data);
    void trainLayer(double[] outputs, float ratio);
    void setOutputs(double[] outputs);
    void findErrors(double[] errors, double[][] weights);
    void datasetOffsetError(double[] dataset);
    Neuron getNeuron(int index);

    double[][] getWeights();
    double[] getErrors();
    double[] getOutputs();
    double[] getBias();
    AIFunctions getAIFunction();
}
