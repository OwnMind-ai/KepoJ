package ai.engine.kepoj.layers;

import ai.engine.kepoj.functions.ActivationFunction;
import ai.engine.kepoj.functions.StandardFunctions;
import ai.engine.kepoj.entities.Neuron;

import java.util.Collections;

public class ConvolutionalLayer implements Layer {
    private final ActivationFunction aiFunctions;
    private Neuron[] neurons;
    private int layerSizeX = Integer.MIN_VALUE;
    private int layerSizeY = Integer.MIN_VALUE;
    private int previousLayerX;
    private int previousLayerY;
    private final int coreSizeX;
    private final int coreSizeY;

    public ConvolutionalLayer(int coreSizeX, int coreSizeY,
                              ActivationFunction aiFunctions){
        this.aiFunctions = aiFunctions;

        this.coreSizeX = coreSizeX;
        this.coreSizeY = coreSizeY;
    }

    public ConvolutionalLayer(int coreSizeX, int coreSizeY,
                              StandardFunctions aiFunctions){
        this(coreSizeX, coreSizeY, aiFunctions.get());
    }

    private static int calculateLayersSize(int coreSize, int previousLayerSize){
        return previousLayerSize - coreSize + 1;
    }

    public void setSize(int width, int height){
        this.layerSizeX = width;
        this.layerSizeY = height;
    }

    public int getWidth() { return this.layerSizeX; }

    public int getHeight() { return this.layerSizeY; }
    private int getIdByPosition(int x, int y){
        return y * this.previousLayerX + x;
    }

    public double[] calculateWeights(int corePositionX, int corePositionY){
        double[] weights = new double[this.previousLayerX * this.previousLayerY];

        for(int y = corePositionY; y < this.coreSizeY + corePositionY; y++)
            for (int x = corePositionX; x < this.coreSizeX + corePositionX; x++)
                weights[this.getIdByPosition(x, y)] = Neuron.random();

        return weights;
    }

    @Override
    public void buildLayer(double... data) {
        assert data.length >= 2 : "Layer data in wrong format";
        this.previousLayerX = (int) data[0];
        this.previousLayerY = (int) data[1];
        if (this.layerSizeX < 0 && this.layerSizeY < 0) {
            this.layerSizeX = calculateLayersSize(this.coreSizeX, this.previousLayerX);
            this.layerSizeY = calculateLayersSize(this.coreSizeY, this.previousLayerY);
        }

        this.neurons = new Neuron[layerSizeX * layerSizeY];

        for(int i = 0; i < this.neurons.length; i++){
            this.neurons[i] = new Neuron(
                this.calculateWeights(
                        i % this.layerSizeX,
                        (int) Math.floor((double) i / this.layerSizeY)),
                Neuron.random(),
                this.aiFunctions
            );
        }
    }

    @Override
    public void doLayer(double[] data) {
        for(Neuron neuron : this.neurons) neuron.excite(data);
    }

    @Override
    public void trainLayer(double[] outputs, double ratio) {
        for (Neuron neuron : this.neurons) {
            for (int j = 0; j < neuron.weights.size(); j++)
                if (neuron.weights.get(j) != null) {
                    neuron.weights.set(j,
                            neuron.weights.get(j) + ratio * outputs[j] * neuron.error
                    );
                }
            neuron.bias += ratio * neuron.error;
        }
    }

    @Override
    public void setOutputs(double[] outputs) {
        for(int i = 0; i < this.neurons.length; i++)
            this.neurons[i].output = outputs[i];
    }

    @Override
    public void findErrors(double[] errors, double[][] weights) {
        for(int i = 0; i < this.neurons.length; i++) {
            double error = 0;
            for(int j = 0; j < errors.length;j++)
                error+= errors[j] * (Collections.singletonList(weights[j][i]).get(0) == null ? 0 : weights[j][i]);

            this.neurons[i].setError(error);
        }
    }

    @Override
    public void setErrors(double[] errors) {
        for(int i = 0; i < this.neurons.length; i++)
            this.neurons[i].setError(errors[i]);
    }

    @Override
    public Neuron getNeuron(int index) {
        return this.neurons[index];
    }

    @Override
    public double[][] getWeights() {
        double[][] result = new double[this.neurons.length][];
        for(int i = 0; i < this.neurons.length; i++)
            result[i] = this.neurons[i].weights.stream().mapToDouble(Double::doubleValue).toArray();

        return result;
    }

    @Override
    public double[] getErrors() {
        double[] result = new double[this.neurons.length];
        for(int i = 0; i < this.neurons.length; i++)
            result[i] = this.neurons[i].error;

        return result;
    }

    @Override
    public double[] getOutputs() {
        double[] result = new double[this.neurons.length];
        for(int i = 0; i < this.neurons.length; i++)
            result[i] = this.neurons[i].output;

        return result;
    }

    @Override
    public double[] getBias() {
        double[] result = new double[this.neurons.length];
        for(int i = 0; i < this.neurons.length; i++)
            result[i] = this.neurons[i].bias;

        return result;
    }

    @Override
    public int length() {
        return this.neurons.length;
    }
}
