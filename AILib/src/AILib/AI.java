package AILib;

import AILib.AILib.AIFunctions;
import AILib.AILib.Dataset;
import AILib.AILib.FileHandler;
import AILib.AILib.Neuron;

import java.util.ArrayList;
import java.util.Arrays;

public class AI{
    private ArrayList<ArrayList<Neuron>> neurons;
    public float fault = 0.0005f;
    private AIFunctions taf;
    public String id;

    public AI(int inputNeurons, AIFunctions functionsType){
        this.id = this.toString();
        buildAI(inputNeurons, functionsType);
    }

    public AI(String fileName){
        this.id = fileName.split("\\.")[0];
        this.id = this.id.split("/")[this.id.split("/").length - 1];

        double[] AIParameters = FileHandler.readFile(fileName);

        assert(AIParameters != null);
        this.buildAI((int) AIParameters[1],AIFunctions.values()[(int) AIParameters[(int) AIParameters[0]]]);
        for(int i = 2; i < (int) AIParameters[0]; i++) {
            this.addLayer((int) AIParameters[i]);
        }

        double[] weights = new double[(int) (AIParameters.length - AIParameters[0] - 1)];
        System.arraycopy(AIParameters, (int) AIParameters[0] + 1, weights, 0, weights.length);
        this.setWeights(weights);
    }

    private void buildAI(int inputNeurons, AIFunctions functionsType){
        this.neurons = new ArrayList<>();
        this.neurons.add(new ArrayList<>());
        for (int a = 0; a < inputNeurons; a++) {
            this.neurons.get(0).add(new Neuron(1,functionsType));
        }
        this.taf = functionsType;
    }

    public void setFault(float fault) {
        this.fault = fault;
    }

    public void addLayer(int neuronsCount) {
        this.neurons.add(new ArrayList<>());
        int index = this.neurons.size() - 1;
        for (int i = 0; i < neuronsCount; i++) {
            this.neurons.get(index).add(new Neuron(this.neurons.get(index - 1).size(), this.taf));
        }
    }

    public double[] start(double[] inputData){
        this.loadTask(inputData);

        for (int i = 1; i < this.neurons.size(); i++) {
            double[] layerOutput = new double[this.neurons.get(i-1).size()];
            for (int a = 0; a < this.neurons.get(i-1).size(); a++)
                layerOutput[a] = this.neurons.get(i-1).get(a).output;

            for (int a = 0; a < this.neurons.get(i).size(); a++)
                this.neurons.get(i).get(a).doNeuron(layerOutput);
        }
        
        double[] output = new double[this.neurons.get(this.neurons.size() - 1).size()];
        for (int a = 0; a < this.neurons.get(this.neurons.size() - 1).size(); a++){
            output[a] = this.neurons.get(this.neurons.size() - 1).get(a).output;
        }
        return output;
    }

    private void loadTask(double[] inputArray) {
        for (int i = 0; i < inputArray.length; i++) {
            this.neurons.get(0).get(i).output = inputArray[i];
        }
    }

    private void outError(double[] output){
        for (int i = 0; i < output.length; i++)
            this.neurons.get(this.neurons.size() - 1).get(i).setError(output[i] - this.neurons.get(this.neurons.size() - 1).get(i).output);
    }

    private void findError(){
        for(int i = this.neurons.size()-2; i > 0; i--){
            for(int a = 0; a < this.neurons.get(i).size(); a++){
                double error = 0;
                for(int b = 0; b < this.neurons.get(i+1).size(); b++){
                    error+= this.neurons.get(i+1).get(b).weights.get(a) * this.neurons.get(i+1).get(b).error;
                }
                this.neurons.get(i).get(a).setError(error);
            }
        }
    }

    private void backWeights(float ratio) {
        for (int i = 1; i < this.neurons.size(); i++) {
            for(int a = 0; a < this.neurons.get(i).size(); a++){
                for (int b = 0; b < this.neurons.get(i).get(a).weights.size(); b++) {
                    this.neurons.get(i).get(a).weights.set(b,this.neurons.get(i).get(a).weights.get(b) + ratio * this.neurons.get(i-1).get(b).output * this.neurons.get(i).get(a).error);
                }
                this.neurons.get(i).get(a).bias+= ratio * this.neurons.get(i).get(a).error;
            }
        }
    }

    public Double[] learning(double[][][] example, float ratio) {
        ArrayList<Double> errorsLog = new ArrayList<>();
        double sumError = 1;

        while (sumError >= this.fault) {
            sumError = 0;
            for (double[][] doubles : example) {
                double[] result = this.start(doubles[0]);
                for (int a = 0; a < result.length; a++)
                    sumError += (float) (Math.pow((doubles[1][a] - result[a]), 2));

                this.outError(doubles[1]);
                this.findError();
                this.backWeights(ratio);
            }
            errorsLog.add(sumError);
        }

        return (Double[]) errorsLog.toArray();
    }

    public void learning(Dataset dataset, float ratio){ this.learning(dataset.getDatasetArray(), ratio); }

    public int[] AIChecker(double[][][] example){
        int[] resultsInfo = {0, 0};
        for(double[][] i : example){
            resultsInfo[0]++;
            double[] answer = this.start(i[0]);
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

    public void AIChecker(Dataset dataset){ this.AIChecker(dataset.getDatasetArray()); }

    public double[][][] getWeights() {
        double[][][] weights = new double[neurons.size()-1][][];
        for(int i = 1; i < this.neurons.size(); i++){
            weights[i-1] = new double[this.neurons.get(i).size()][];
            for(int a = 0; a < weights[i-1].length; a++) {
                double[] neuronWeights = new double[this.neurons.get(i).get(a).weights.size() + 1];
                for (int b = 0; b < this.neurons.get(i).get(a).weights.size(); b++)
                    neuronWeights[b] = this.neurons.get(i).get(a).weights.get(b);
                neuronWeights[this.neurons.get(i).get(a).weights.size()] = this.neurons.get(i).get(a).bias;

                weights[i-1][a] = neuronWeights;
            }
        }
        return weights;
    }

    public void setWeights(double[] weights) {
        int index = 0;
        for(int i = 1; i < this.neurons.size(); i++)
            for(int a = 0; a < this.neurons.get(i).size(); a++)
                for(int b = 0; b < this.neurons.get(i).get(a).weights.size() + 1; b++)
                    this.neurons.get(i).get(a).setWeight(b, weights[index++]);
    }

    public void saveAI(String fileName){
        double[] weights = Arrays.stream(this.getWeights()).flatMap(Arrays::stream).flatMapToDouble(Arrays::stream).toArray();
        double[] AIParameters = new double[this.neurons.size() + 2];  //first element - list size(need to handle data in file), last element - AIFunction id
        AIParameters[0] = AIParameters.length - 1;
        for(int i = 1; i < AIParameters.length - 1; i++)
            AIParameters[i] = this.neurons.get(i - 1).size();
        AIParameters[AIParameters.length - 1] = Arrays.asList(AIFunctions.values()).indexOf(this.taf);

        double[] result = Arrays.copyOf(AIParameters, weights.length + AIParameters.length);
        System.arraycopy(weights, 0, result, AIParameters.length, weights.length);

        FileHandler.writeToFile(result, fileName);
    }

    public void saveAI(){ this.saveAI(this.toString() + ".bin");}
    
    public String getAIParameters(){
        int[] output = new int[this.neurons.size()];
        for (int i = 0; i < this.neurons.size(); i++)
            output[i] = this.neurons.get(i).size();
        return Arrays.toString(output);
    }
}