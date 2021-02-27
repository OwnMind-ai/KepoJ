package AILib;

import AILib.AILib.AIFunctions;

import java.util.ArrayList;
import java.util.Random;

public class Neuron{
    public double output = 0;
    public double error = 0;
    private final AIFunctions activationFunc;

    public final static Random random = new Random();

    public ArrayList<Double> weights;
    public double bias;

    public Neuron(int weightsCount, AIFunctions activationFunc){
        this.activationFunc = activationFunc;
        this.weights = new ArrayList<>();

        for(int i = 0; i < weightsCount; i++){
            this.weights.add(Neuron.random.nextDouble());
        }
        this.bias = Neuron.random.nextDouble();
    }

    public void doNeuron(double[] inputData){
        this.output = 0;
        for(int i = 0; i < inputData.length; i++)
            this.output+= inputData[i] * this.weights.get(i);
        this.output+= this.bias;
        this.output = this.activationFunc.activationRun(this.output);
    }

    public void setWeight(int index, double value) {
        if(index == this.weights.size()) this.bias = value;
        else this.weights.set(index, value);
    }

    public void setError(double error) {
        this.error = error * this.activationFunc.derivativeRun(this.output);
    }
}