package ai.engine.kepoj.layers;

import ai.engine.kepoj.functions.ActivationFunction;
import ai.engine.kepoj.functions.StandardFunctions;
import ai.engine.kepoj.entities.Neuron;

import java.util.ArrayList;
import java.util.List;

/**
 * Layer implementation that uses a dynamic list to store the neurons and automatically affects the dependent layer.
 * @see Layer
 * @since 1.2
 */
public class DynamicLayer implements Layer{
    private final ActivationFunction aiFunctions;
    private final List<Neuron> neurons;
    private final int initialSize;
    private final Layer nextLayer;

    /**
     * Creates a layer with resizable neurons list
     * @param initialSize number of neurons after initialization
     * @param functions activation function complex for this layer
     * @param nextLayer dependent layer whose weight lengths will change according to changes in the dynamic layer
     * @since 1.2
     */
    public DynamicLayer(int initialSize, ActivationFunction functions, Layer nextLayer){
        this.aiFunctions = functions;
        
        this.neurons = new ArrayList<>();
        this.initialSize = initialSize;
        this.nextLayer = nextLayer;
    }

    /**
     * Creates a layer with resizable neurons list
     * @param initialSize number of neurons after initialization
     * @param function defined function from the StandardFunction enum
     * @param nextLayer dependent layer whose weight lengths will change according to changes in the dynamic layer
     * @since 1.2
     */
    public DynamicLayer(int initialSize, StandardFunctions function, Layer nextLayer){
        this(initialSize, function.get(), nextLayer);
    }

    /**
     * Adds neuron to layer and sets specific weights between the neuron and the next layer
     * @param neuron neuron to add
     * @param weights weights for this neuron on the next layer
     * @return added neuron index
     * @since 1.2
     */
    public int add(Neuron neuron, double[] weights){
        this.neurons.add(neuron);

        for (int i = 0; i < this.nextLayer.length(); i++)
            this.nextLayer.getNeuron(i).weights.add(weights[i]);

        return this.neurons.size() - 1;
    }

    /**
     *  Adds neuron to layer with random weights between the neuron and the next layer
     * @param neuron neuron to add
     * @return added neuron index
     * @since 1.2
     */
    public int add(Neuron neuron){
        this.neurons.add(neuron);

        for (int i = 0; i < this.nextLayer.length(); i++)
            this.nextLayer.getNeuron(i).weights.add(Neuron.random());

        return this.neurons.size() - 1;
    }

    /**
     * Removes a neuron from a layer by neuron index
     * @param index neuron index to remove
     */
    public void remove(int index){
        this.neurons.remove(index);

        for (int i = 0; i < this.nextLayer.length(); i++)
            this.nextLayer.getNeuron(i).weights.remove(index);
    }

    /**
     * Takes a length of the previous layer and uses it as a length of neurons weight.
     * Usually, Layer must be built in the neural network class.
     * @param previous layer object before current layer
     * @since 1.1
     */
    @Override
    public void buildLayer(Layer previous){
        assert previous != null : "Previous layer is null";

        for (int i = 0; i < initialSize; i++)
            this.neurons.add(new Neuron(previous.length(), this.aiFunctions));
    }

    /**
     * Processes data through every neuron
     * @param data input data with the same size as length of previous layer
     * @since 1.1
     */
    @Override
    public void doLayer(double[] data) {
        for(Neuron neuron : this.neurons)
            neuron.excite(data);
    }

    @Override
    public double[] getOutputs() {
        return this.neurons.stream().mapToDouble(x -> x.output).toArray();
    }

    @Override
    public double[] getErrors() {
        return this.neurons.stream().mapToDouble(x -> x.error).toArray();
    }

    @Override
    public double[][] getWeights() {
        double[][] result = new double[this.neurons.size()][];
        for(int i = 0; i < this.neurons.size(); i++)
            result[i] = this.neurons.get(i).weights.stream().mapToDouble(Double::doubleValue).toArray();

        return result;
    }

    @Override
    public double[] getBias() {
        return this.neurons.stream().mapToDouble(x -> x.bias).toArray();
    }

    /**
     * Returns count of the neurons on the layer
     * @return layer length
     * @since 1.1
     */
    @Override
    public int length() {
        return this.neurons.size();
    }

    @Override
    public Neuron getNeuron(int index) {
        return this.neurons.get(index);
    }

    /**
     * Calculates neuron errors based on the next layer's errors and weights between them
     * @param errors errors on the next layer
     * @param weights weights between current and next layers
     * @since 1.1
     */
    @Override
    public void findErrors(double[] errors, double[][] weights) {
        for(int i = 0; i < this.neurons.size(); i++) {
            double error = 0;
            for(int j = 0; j < errors.length;j++)
                error+= errors[j] * weights[j][i];

            this.neurons.get(i).setError(error);
        }
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
                neuron.weights.set(j,
                        neuron.weights.get(j) + ratio * outputs[j] * neuron.error
                );
            neuron.bias += ratio * neuron.error;
        }
    }

    @Override
    public void setOutputs(double[] outputs) {
        for(int i = 0; i < this.neurons.size(); i++)
            this.neurons.get(i).output = outputs[i];
    }
}
