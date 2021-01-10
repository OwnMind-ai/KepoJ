package AILib;

import AILib.AILib.AIFunctions;

import java.util.ArrayList;
import java.util.Random;

public class Neuron{
    public float output = 0;
    public float error = 0;
    private final AIFunctions activationFunc;

    public final static Random random = new Random();

    public ArrayList<Float> weights;
    public float bias;

    public Neuron(int weightsCount, AIFunctions activationFunc){
        this.activationFunc = activationFunc;
        this.weights = new ArrayList<>();

        for(int i = 0; i < weightsCount; i++){
            this.weights.add(Neuron.random.nextFloat());
        }
        this.bias = Neuron.random.nextFloat();
    }

    public void DoNeuron(float[] inputData){
        this.output = 0;
        for(int i = 0; i < inputData.length; i++)
            this.output+= inputData[i] * this.weights.get(i);
        this.output+= this.bias;
        this.output = this.activationFunc.ActivationRun(this.output);

    }

    public void SetError(float error) {
        this.error = error * this.activationFunc.DerivativeRun(this.output);
    }
}