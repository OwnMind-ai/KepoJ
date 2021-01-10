package AILib;

import AILib.AILib.AIFunctions;
import java.util.ArrayList;
import java.util.Arrays;

public class AI{
    private final ArrayList<ArrayList<Neuron>> neurons;
    public float fault = 0.0005f;
    private final AIFunctions taf;
    public int age;
    public boolean doStop = false;

    public String ArrayToString(float[][][] array) {
        StringBuilder str = new StringBuilder("[");
        for (float[][] floats : array) {
            str.append("\n[");
            for (float[] aFloat : floats) {
                str.append(Arrays.toString(aFloat));
                str.append(",\n");
            }
            str.deleteCharAt(str.length() - 1);
            str.append("]\n");
        }
        str.append(']');

        return str.toString();
    }
    public String ArrayToString(float[][] array) {
        StringBuilder str = new StringBuilder("[");
        for (float[] floats : array) {
            str.append(Arrays.toString(floats));
            str.append(",\n");
        }
        str.append(']');

        return str.toString();
    }

    public AI(int inputNeurons, AIFunctions functionsType){
        this.neurons = new ArrayList<>();
        this.neurons.add(new ArrayList<>());
        for (int a = 0; a < inputNeurons; a++) {
            this.neurons.get(0).add(new Neuron(1,functionsType));
        }
        this.taf = functionsType;
    }

    public void Settings(float fault) {
        this.fault = fault;
    }

    public void AddLayer(int neuronsCount) {
        this.neurons.add(new ArrayList<>());
        int index = this.neurons.size() - 1;
        for (int i = 0; i < neuronsCount; i++) {
            this.neurons.get(index).add(new Neuron(this.neurons.get(index - 1).size(), this.taf));
        }
    }

    public float[] Start(float[] inputData){
        this.LoadTask(inputData);

        for (int i = 1; i < this.neurons.size(); i++) {
            float[] layerOutput = new float[this.neurons.get(i-1).size()];
            for (int a = 0; a < this.neurons.get(i-1).size(); a++)
                layerOutput[a] = this.neurons.get(i-1).get(a).output;

            for (int a = 0; a < this.neurons.get(i).size(); a++)
                this.neurons.get(i).get(a).DoNeuron(layerOutput);
        }
        
        float[] output = new float[this.neurons.get(this.neurons.size() - 1).size()];
        for (int a = 0; a < this.neurons.get(this.neurons.size() - 1).size(); a++){
            output[a] = this.neurons.get(this.neurons.size() - 1).get(a).output;
        }
        return output;
    }

    private void LoadTask(float[] inputArray) {
        for (int i = 0; i < inputArray.length; i++) {
            this.neurons.get(0).get(i).output = inputArray[i];
        }
    }

    private void OutError(float[] output){
        for (int i = 0; i < output.length; i++)
            this.neurons.get(this.neurons.size() - 1).get(i).SetError(output[i] - this.neurons.get(this.neurons.size() - 1).get(i).output);
    }

    private void FindError(){
        for(int i = this.neurons.size()-2; i > 0; i--){
            for(int a = 0; a < this.neurons.get(i).size(); a++){
                float error = 0;
                for(int b = 0; b < this.neurons.get(i+1).size(); b++){
                    error+= this.neurons.get(i+1).get(b).weights.get(a) * this.neurons.get(i+1).get(b).error;
                }
                this.neurons.get(i).get(a).SetError(error); 
            }
        }
    }

    private void BackWeights(float ratio) {
        for (int i = 1; i < this.neurons.size(); i++) {
            for(int a = 0; a < this.neurons.get(i).size(); a++){
                for (int b = 0; b < this.neurons.get(i).get(a).weights.size(); b++) {
                    this.neurons.get(i).get(a).weights.set(b,this.neurons.get(i).get(a).weights.get(b) + ratio * this.neurons.get(i-1).get(b).output * this.neurons.get(i).get(a).error);
                }
                this.neurons.get(i).get(a).bias+= ratio * this.neurons.get(i).get(a).error;
            }
        }
    }

    public void Learning(float[][][] example, float ratio) {
        this.age = 0;
        float sumError = 1;

        while (sumError >= this.fault && !this.doStop) {
            sumError = 0;
            for (float[][] floats : example) {
                float[] result = this.Start(floats[0]);
                for (int a = 0; a < result.length; a++)
                    sumError += (float) (Math.pow((floats[1][a] - result[a]), 2));

                this.OutError(floats[1]);
                this.FindError();
                this.BackWeights(ratio);
            }
            this.age++;
            //System.out.println(age + "-" + sumError);
        }
    }

    public int[] AICheaker(float[][][] example){
        int[] resultsInfo = {0, 0};
        for(float[][] i : example){
            resultsInfo[0]++;
            float[] answer = this.Start(i[0]);
            for(int a = 0; a < answer.length; a++) {
                answer[a] = Math.round(answer[a] * 10f) / 10f;
            }
            if(Arrays.equals(answer,i[1]))
                resultsInfo[1]++;

            System.out.print("Output: " + Arrays.toString(answer) + " Example: " + Arrays.toString(i[1]) + "\n");
        }
        System.out.print(Arrays.toString(resultsInfo) + "\n");

        return resultsInfo;
    }

    public float[][][] GetWeights() {
        float[][][] weights = new float[neurons.size()-1][][];
        for(int i = 1; i < this.neurons.size(); i++){
            weights[i-1] = new float[this.neurons.get(i).size()][];
            for(int a = 0; a < weights[i-1].length; a++) {
                float[] neuronWeights = new float[this.neurons.get(i).get(a).weights.size() + 1];
                for (int b = 0; b < this.neurons.get(i).get(a).weights.size(); b++)
                    neuronWeights[b] = this.neurons.get(i).get(a).weights.get(b);
                neuronWeights[this.neurons.get(i).get(a).weights.size()] = this.neurons.get(i).get(a).bias;

                weights[i-1][a] = neuronWeights;
            }
        }
        return weights;
    }

    public float[][] GetErrors() {
        float[][] errors = new float[neurons.size()-1][];
        for(int i = 1; i < this.neurons.size(); i++){
            errors[i-1] = new float[this.neurons.get(i).size()];
            for(int a = 0; a < errors[i-1].length; a++) {
               errors[i-1][a] = this.neurons.get(i).get(a).error;
            }
        }
        return errors;
    }
}