package ai.engine.kepoj.entities;

import ai.engine.kepoj.exceptions.NeuralNetworkRuntimeException;
import ai.engine.kepoj.functions.ActivationFunction;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The fundamental unit of NeuralNetwork class. Each instance has a List
 * of weights that can be resized in the DynamicLayer class or anywhere else.
 * The bias neuron is moved to a variable that modifying separately from the list of weights.
 * @see ai.engine.kepoj.agents.deep.NeuralNetwork
 * @since 1.0
 */
public class Neuron implements Serializable {
    /**
     * Contains the output of the neuron before activation (rawOutput) and after (output)
     * @since 1.0
     */
    public double output, rawOutput = 0;
    /**
     * Contains an error of neuron. Can't be serialized.
     * @since 1.0
     */
    public transient double error = 0;
    private final ActivationFunction neuronFunctions;
    /**
     * Resizable list of the neuron weights.
     * @since 1.0
     */
    public ArrayList<Double> weights;
    public double bias;

    /**
     * Creates a neuron object with randomly generated weights and bias
     * @param weightsCount count of input connections to the neuron, usually is neurons count in the previous layer.
     * @param activationFunc activation function
     */
    public Neuron(int weightsCount, ActivationFunction activationFunc){
        this.neuronFunctions = activationFunc;
        this.weights = new ArrayList<>();

        assert weightsCount > 0 : new NeuralNetworkRuntimeException("Neuron haven't any weights");
        for(int i = 0; i < weightsCount; i++)
            this.weights.add(Neuron.random());
        this.bias = Neuron.random();
    }

    /**
     * Creates a neuron object with specific weights and bias values
     * @param weights weights array
     * @param bias weight for bias-neuron
     * @param activationFunc activation function
     */
    public Neuron(double[] weights, double bias, ActivationFunction activationFunc){
        this.neuronFunctions = activationFunc;
        this.weights = new ArrayList<>();

        assert weights.length > 0 : new NeuralNetworkRuntimeException("Neuron haven't any weights");
        for (double weight : weights) this.weights.add(weight);
        this.bias = bias;
    }


     /**
     * Generates a random number for neuron weights
     * @return random number between -1 and 1
     */
    public static double random(){
        return Math.random() * 2 - 1;
    }

    /**
     * Processes the input by multiplying each input value by the corresponding weight and summing it.
     * The sum is written to Neuron::rawOutput, after that the sum is activated by activation function and writes to
     * the Neuron::output variable.
     * @param inputData input values for neuron
     * @return activated sum
     * @since 1.0
     */
    public double excite(double[] inputData){
        assert inputData.length == this.weights.size() :
            new NeuralNetworkRuntimeException("Invalid input data to neuron: " + inputData.length);

        this.rawOutput = 0;
        for(int i = 0; i < inputData.length; i++)
            this.rawOutput+= inputData[i] * (this.weights.get(i) == null ? 0 : this.weights.get(i));
        this.rawOutput+= this.bias;
        this.output = this.neuronFunctions.activate(this.rawOutput);

        return this.output;
    }

    @Deprecated
    public void setWeight(int index, double value) {
        if(index == this.weights.size()) this.bias = value;
        else this.weights.set(index, value);
    }

    /**
     * Sets the neuron's error and multiplies it by the derivative of the neuron output
     * @param error value of the neuron's error without multiplying by the derivative of the output
     * @since 1.1
     */
    public void setError(double error) {
        this.error = error * this.neuronFunctions.derivative(this.rawOutput);
    }
}