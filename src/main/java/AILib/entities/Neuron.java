package AILib.entities;

import AILib.functions.ActivationFunction;

import java.util.ArrayList;
import java.util.Random;

public class Neuron{
    public double output = 0;
    public double error = 0;
    private final ActivationFunction neuronFunctions;

    public final static Random random = new Random();

    public ArrayList<Double> weights;
    public double bias;

    public Neuron(int weightsCount, ActivationFunction activationFunc){
        this.neuronFunctions = activationFunc;
        this.weights = new ArrayList<>();

        for(int i = 0; i < weightsCount; i++)
            this.weights.add(Neuron.random.nextDouble() * 2 - 1);
        this.bias = Neuron.random.nextDouble() * 2 - 1;
    }

    public Neuron(double[] weights, ActivationFunction activationFunc){
        this.neuronFunctions = activationFunc;
        this.weights = new ArrayList<>();

        assert weights.length > 0 : "Neuron haven't any weights";
        for(int i = 0; i < weights.length - 1; i++)
            this.weights.add(weights[i]);
        this.bias = weights[weights.length - 1];
    }

    public double excite(double[] inputData){
        this.output = 0;
        for(int i = 0; i < inputData.length; i++)
            this.output+= inputData[i] * (this.weights.get(i) == null ? 0 : this.weights.get(i));
        this.output+= this.bias;
        this.output = this.neuronFunctions.activate(this.output);

        return output;
    }

    public void setWeight(int index, double value) {
        if(index == this.weights.size()) this.bias = value;
        else this.weights.set(index, value);
    }

    public void setError(double error) {
        this.error = error * this.neuronFunctions.derivative(this.output);
    }
}