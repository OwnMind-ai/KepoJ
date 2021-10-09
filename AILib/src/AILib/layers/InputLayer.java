package AILib.layers;

import AILib.entities.Neuron;

public class InputLayer implements Layer{
    private double[] outputs;

    public InputLayer(int neuronCount){
        this.outputs = new double[neuronCount];
    }

    @Override
    public void buildLayer(int weightsCount) {

    }

    @Override
    public double[] doLayer(double[] data) {
        return new double[0];
    }

    @Override
    public void trainLayer(double[] outputs, float ratio) {

    }

    @Override
    public void setOutputs(double[] outputs) {
        this.outputs = outputs;
    }

    @Override
    public void findErrors(double[] errors, double[][] weights) {

    }

    @Override
    public void setErrors(double[] dataset) {

    }

    @Override
    public Neuron getNeuron(int index) {
        return null;
    }

    @Override
    public double[][] getWeights() {
        return new double[0][];
    }

    @Override
    public double[] getErrors() {
        return new double[0];
    }

    @Override
    public double[] getOutputs() {
        return this.outputs;
    }

    @Override
    public double[] getBias() {
        return new double[0];
    }

    @Override
    public int getNeuronsLength() {
        return this.outputs.length;
    }

    @Override
    public double[] getArchivedData() {
        return new double[]{this.getNeuronsLength()};
    }
}
