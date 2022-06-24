package ai.engine.kepoj.layers;

import ai.engine.kepoj.entities.Neuron;
import ai.engine.kepoj.functions.ActivationFunction;
import ai.engine.kepoj.functions.StandardFunctions;

import java.util.Collections;

import static ai.engine.kepoj.agents.deep.NeuralNetwork.WARNINGS;

/**
 * An implementation of a convolutional layer that restrict neurons weights to their scopes (core) in two dimensions.
 * @since 1.1
 */
public class ConvolutionalLayer implements Layer {
    private final ActivationFunction aiFunctions;
    private Neuron[] neurons;
    private int layerWidth = Integer.MIN_VALUE;
    private int layerHeight = Integer.MIN_VALUE;
    private int previousWidth = Integer.MIN_VALUE;
    private int previousHeight = Integer.MIN_VALUE;
    private final int coreWidth;
    private final int coreHeight;

    /**
     * Creates a convolutional layer with a fixed core size
     * @param coreWidth neuron's scope width
     * @param coreHeight neuron's scope height
     * @param functions activation function complex for this layer
     * @since 1.1
     */
    public ConvolutionalLayer(int coreWidth, int coreHeight,
                              ActivationFunction functions){
        this.aiFunctions = functions;

        this.coreWidth = coreWidth;
        this.coreHeight = coreHeight;
    }

    /**
     * Creates a convolutional layer with a fixed core size
     * @param coreWidth neuron's scope width
     * @param coreHeight neuron's scope height
     * @param functions defined function from the StandardFunction enum
     * @since 1.1
     */
    public ConvolutionalLayer(int coreWidth, int coreHeight,
                              StandardFunctions functions){
        this(coreWidth, coreHeight, functions.get());
    }

    /**
     * Creates a convolutional layer with a fixed core size and sets
     * @param coreWidth neuron's scope width
     * @param coreHeight neuron's scope height
     * @param layerWidth constant layer width
     * @param layerHeight constant layer height
     * @param functions activation function complex for this layer
     * @since 1.2
     */
    public ConvolutionalLayer(int coreWidth, int coreHeight, int layerWidth, int layerHeight,
                              ActivationFunction functions){
        this.aiFunctions = functions;

        this.coreWidth = coreWidth;
        this.coreHeight = coreHeight;
        this.layerWidth = layerWidth;
        this.layerHeight = layerHeight;
    }

    /**
     * Creates a convolutional layer with a fixed core size and sets
     * @param coreWidth neuron's scope width
     * @param coreHeight neuron's scope height
     * @param layerWidth constant layer width
     * @param layerHeight constant layer height
     * @param functions defined function from the StandardFunction enum
     * @since 1.2
     */
    public ConvolutionalLayer(int coreWidth, int coreHeight, int layerWidth, int layerHeight,
                              StandardFunctions functions){
        this(coreWidth, coreHeight, layerWidth, layerHeight, functions.get());
    }

    private static int calculateLayersSize(int coreSize, int previousLayerSize){
        return previousLayerSize - coreSize + 1;
    }

    /** Sets a constant layer size. Use only before layer building.
     * @param width layer width
     * @param height layer height
     * @since 1.2
     */
    public void setSize(int width, int height){
        this.layerWidth = width;
        this.layerHeight = height;
    }

    public int getWidth() { return this.layerWidth; }

    public int getHeight() { return this.layerHeight; }
    private int getIdByPosition(int x, int y){
        return y * this.previousWidth + x;
    }

    /**
     * Calculates weights array for the neuron on the provided position
     * @param corePositionX neuron position x
     * @param corePositionY neuron position y
     * @return weights for neuron
     * @since 1.1
     */
    private double[] calculateWeights(int corePositionX, int corePositionY){
        double[] weights = new double[this.previousWidth * this.previousHeight];

        for(int y = corePositionY; y < this.coreHeight + corePositionY; y++)
            for (int x = corePositionX; x < this.coreWidth + corePositionX; x++)
                weights[this.getIdByPosition(x, y)] = Neuron.random();

        return weights;
    }

    /**
     * Determines the size of the layer based on structure of the previous layer
     * @param previous previous layer
     * @since 1.2
     */
    private void assignPreviousSize(Layer previous){
        if(this.previousWidth <= 0 && this.previousHeight <= 0) {
            if (previous instanceof ConvolutionalLayer) {
                ConvolutionalLayer layer = (ConvolutionalLayer) previous;
                this.previousWidth = layer.layerWidth;
                this.previousHeight = layer.layerHeight;
            } else {
                this.previousWidth = (int) Math.floor(Math.sqrt(previous.length()));
                this.previousHeight = previous.length() / this.previousWidth;

                if (WARNINGS && !(previous instanceof InputLayer))
                    System.err.println(
                            "KepoJ Warning: layer not a ConvolutionLayer and located before another ConvolutionLayer." +
                                    "Size of next layer calculated as square of previous layer's length"
                    );
            }
        }
    }

    /** Determines layer size and initializes neurons array. If previous layer is instance of the Convolution layer,
     * size of current layer will be based on size of the previous layer. Otherwise, size of previous layer takes as
     * rectangle with area equals layer length.
     * Usually, Layer must be built in the neural network class.
     * @param previous layer object before current layer
     * @since 1.1
     */
    @Override
    public void buildLayer(Layer previous) {
        assert previous != null : "Previous layer is null";

        this.assignPreviousSize(previous);

        if (this.layerWidth < 0 && this.layerHeight < 0) {
            this.layerWidth = calculateLayersSize(this.coreWidth, this.previousWidth);
            this.layerHeight = calculateLayersSize(this.coreHeight, this.previousHeight);
        }

        this.neurons = new Neuron[layerWidth * layerHeight];

        for(int i = 0; i < this.neurons.length; i++){
            this.neurons[i] = new Neuron(
                this.calculateWeights(
                        i % this.layerWidth,
                        (int) Math.floor((double) i / this.layerHeight)),
                Neuron.random(),
                this.aiFunctions
            );
        }
    }

    /**
     * Processes data through every neuron
     * @param data input data with the same size as length of previous layer
     * @since 1.1
     */
    @Override
    public void doLayer(double[] data) {
        for(Neuron neuron : this.neurons) neuron.excite(data);
    }

    /** Updates neurons weights based on their errors
     * @param outputs outputs values on the previous layer
     * @param ratio learning decreasing ratio
     * @since 1.1
     */
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

    /**
     * Calculates neuron errors based on the next layer's errors and weights between them
     * @param errors errors on the next layer
     * @param weights weights between current and next layers
     * @since 1.1
     */
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

    /**
     * Returns count of the neurons on the layer
     * @return layer length
     * @since 1.1
     */
    @Override
    public int length() {
        return this.neurons.length;
    }
}
