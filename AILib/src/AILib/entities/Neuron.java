package AILib.entities;

import java.util.*;

public class Neuron{
    public double output = 0;    //Output value of neuron. Changing after [this::doNeuron] method
    public double error = 0;     //Error of neuron, set by [this::setError] method
    private final AIFunctions neuronFunctions;

    public final static Random random = new Random();

    public ArrayList<Double> weights;
    public double bias;          //Weight, input is always 1

    public Neuron(int weightsCount, AIFunctions activationFunc){
        this.neuronFunctions = activationFunc;
        this.weights = new ArrayList<>();

        //Setting random weights
        for(int i = 0; i < weightsCount; i++)
            this.weights.add(Neuron.random.nextDouble());
        this.bias = Neuron.random.nextDouble();
    }

    public Neuron(double[] weights, AIFunctions activationFunc){
        this.neuronFunctions = activationFunc;
        this.weights = new ArrayList<>();

        for(double weight : weights) {
            this.weights.add(weight);
        }
    }

    //Simulates the direct passage of a dataset through a neuron and sets result to [this.output]
    public void doNeuron(double[] inputData){
        this.output = 0;
        for(int i = 0; i < inputData.length; i++)
            this.output+= inputData[i] * (this.weights.get(i) == null ? 0 : this.weights.get(i));
        this.output+= this.bias;
        this.output = this.neuronFunctions.activationRun(this.output);
    }

    //Sets specific weight or bias to neuron
    public void setWeight(int index, double value) {
        if(index == this.weights.size()) this.bias = value;
        else this.weights.set(index, value);
    }

    //Sets error to [this.error] with pre-multiplication by the derivative
    public void setError(double error) {
        this.error = error * this.neuronFunctions.derivativeRun(this.output);
    }
}