package ai.engine.kepoj.layers;

import ai.engine.kepoj.entities.Neuron;

/**
 * Used to store input data without any processing and also does not have neurons on the layer.
 * @since 1.1
 */
public class InputLayer implements Layer{
    private double[] outputs;

    public InputLayer(int neuronCount){
        this.outputs = new double[neuronCount];
    }

    @Override
    public void buildLayer(Layer previous ) {

    }

    @Override
    public void doLayer(double[] data) {
    }

    @Override
    public void trainLayer(double[] outputs, double ratio) {

    }

    @Override
    public void setOutputs(double[] outputs) {
        this.outputs = outputs;
    }

    @Override
    public void findErrors(double[] errors, double[][] weights) {

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
    public int length() {
        return this.outputs.length;
    }

}
