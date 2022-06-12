package ai.engine.kepoj.entities;

import ai.engine.kepoj.exceptions.NeuralNetworkRuntimeException;
import ai.engine.kepoj.functions.ActivationFunction;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable {
    public double output, rawOutput = 0;
    public transient double error = 0;
    private final ActivationFunction neuronFunctions;
    public ArrayList<Double> weights;
    public double bias;

    public Neuron(int weightsCount, ActivationFunction activationFunc){
        this.neuronFunctions = activationFunc;
        this.weights = new ArrayList<>();

        assert weightsCount > 0 : new NeuralNetworkRuntimeException("Neuron haven't any weights");
        for(int i = 0; i < weightsCount; i++)
            this.weights.add(Neuron.random());
        this.bias = Neuron.random();
    }

    public Neuron(double[] weights, double bias, ActivationFunction activationFunc){
        this.neuronFunctions = activationFunc;
        this.weights = new ArrayList<>();

        assert weights.length > 0 : new NeuralNetworkRuntimeException("Neuron haven't any weights");
        for (double weight : weights) this.weights.add(weight);
        this.bias = bias;
    }

    public static double random(){
        return Math.random() * 2 - 1;
    }

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

    public void setWeight(int index, double value) {
        if(index == this.weights.size()) this.bias = value;
        else this.weights.set(index, value);
    }

    public void setError(double error) {
        this.error = error * this.neuronFunctions.derivative(this.rawOutput);
    }
}