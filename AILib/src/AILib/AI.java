package AILib;

import AILib.AILib.AIFunctions;
import AILib.AILib.Dataset;
import AILib.AILib.FileHandler;
import AILib.AILib.Neuron;
import java.util.ArrayList;
import java.util.Arrays;

public class AI{
    protected ArrayList<ArrayList<Neuron>> neurons;     //Dynamic array of Neurons classes
    public float fault = 0.0005f;       //Minimal error of neural network
    protected AIFunctions aiFunctions;  //Contains activation and derivative functions from AIFunctions enum
    public String id;                   //Id of AI class. Used in AIHandler object

    public AI(int inputNeurons, AIFunctions functionsType){    //Initialization by pre-creating first layer
        this.id = this.toString();                             //Dynamic ID
        buildAI(inputNeurons, functionsType);
    }

    public AI(String fileName){                          //Initialization by import of existing AI
        this.id = fileName.split("\\.")[0];
        this.id = this.id.split("/")[this.id.split("/").length - 1];  // Specific ID(fileName)

        double[] AIParameters = FileHandler.readFile(fileName);       //AI's file data

        assert(AIParameters != null);
        this.buildAI((int) AIParameters[1],AIFunctions.values()[(int) AIParameters[(int) AIParameters[0]]]);
        for(int i = 2; i < (int) AIParameters[0]; i++)
            this.addLayer((int) AIParameters[i]);        //Initialization layers

        double[] weights = new double[(int) (AIParameters.length - AIParameters[0] - 1)];
        System.arraycopy(AIParameters, (int) AIParameters[0] + 1, weights, 0, weights.length);
        this.setWeights(weights);     //Setting weights from file data
    }

    private void buildAI(int inputNeurons, AIFunctions functionsType){    //Building AI structure
        this.neurons = new ArrayList<>();
        this.neurons.add(new ArrayList<>());
        for (int a = 0; a < inputNeurons; a++) {
            this.neurons.get(0).add(new Neuron(1,functionsType));   //Adding Neurons classes to array
        }
        this.aiFunctions = functionsType;
    }

    public void setFault(float fault) {
        this.fault = fault;
    }

    public void addLayer(int neuronsCount) {         //Adds layer to [this.neurons] array and adds neurons to it
        this.neurons.add(new ArrayList<>());         //Adding layer array
        int index = this.neurons.size() - 1;
        for (int i = 0; i < neuronsCount; i++) {
            //Adding initialized Neuron class to layer
            this.neurons.get(index).add(new Neuron(this.neurons.get(index - 1).size(), this.aiFunctions));
        }
    }

    public double[] start(double[] inputData){      //Runs the input array through neural network and returns output
        this.loadTask(inputData);

        for (int i = 1; i < this.neurons.size(); i++) {
            //Getting output data from previous layer to [layerOutput]
            double[] layerOutput = new double[this.neurons.get(i-1).size()];
            for (int a = 0; a < this.neurons.get(i-1).size(); a++)
                layerOutput[a] = this.neurons.get(i-1).get(a).output;

            //Importing previous layer data to every neuron of next layer respectively
            for (int a = 0; a < this.neurons.get(i).size(); a++)
                this.neurons.get(i).get(a).doNeuron(layerOutput);
        }

        //Getting data from output(last) layer of AI to [output]
        double[] output = new double[this.neurons.get(this.neurons.size() - 1).size()];
        for (int a = 0; a < this.neurons.get(this.neurons.size() - 1).size(); a++){
            output[a] = this.neurons.get(this.neurons.size() - 1).get(a).output;
        }
        return output;
    }

    protected void loadTask(double[] inputArray) {       //Loads input array to first layer neurons respectively
        for (int i = 0; i < inputArray.length; i++) {
            this.neurons.get(0).get(i).output = inputArray[i];  //Importing data to array
        }
    }

    protected void outError(double[] output){           //Calculates error of output layer
        for (int i = 0; i < output.length; i++)
            this.neurons.get(this.neurons.size() - 1).get(i).setError(output[i] - this.neurons.get(this.neurons.size() - 1).get(i).output);
    }

    protected void findError(){                         //Calculates error of all neurons(without output layer)
        for(int i = this.neurons.size()-2; i > 0; i--){
            for(int a = 0; a < this.neurons.get(i).size(); a++){
                double error = 0;
                for(int b = 0; b < this.neurons.get(i+1).size(); b++)
                    error+= this.neurons.get(i+1).get(b).weights.get(a) * this.neurons.get(i+1).get(b).error;
                this.neurons.get(i).get(a).setError(error);
            }
        }
    }

    protected void backWeights(float ratio) {    //Changing weights of neurons. Ratio - learning coefficient
        for (int i = 1; i < this.neurons.size(); i++) {
            for(int a = 0; a < this.neurons.get(i).size(); a++){
                for (int b = 0; b < this.neurons.get(i).get(a).weights.size(); b++) {
                    //Adding correcting value to weight
                    this.neurons.get(i).get(a).weights.set(b,
                            this.neurons.get(i).get(a).weights.get(b) +
                            this.neurons.get(i-1).get(b).output *
                            this.neurons.get(i).get(a).error *
                            ratio);
                }
                //Adding correcting value to bias
                this.neurons.get(i).get(a).bias+= ratio * this.neurons.get(i).get(a).error;
            }
        }
    }

    public Double[] learning(double[][][] example, float ratio){  //Trains AI by following dataset arrat and learning ratio
        ArrayList<Double> errorsLog = new ArrayList<>();
        double sumError = Double.MAX_VALUE;     //(setting to maximum value for first while iteration)

        while (sumError >= this.fault) {    //Training continues until the error is less than the minimum([this.fault])
            sumError = 0;
            for (double[][] data : example) {
                //Calculating current error by example
                double[] result = this.start(data[0]);
                for (int a = 0; a < result.length; a++)
                    sumError += Math.pow((data[1][a] - result[a]), 2);

                this.outError(data[1]);        //Calculates error of output layer
                this.findError();              //Calculates error of all neurons(without output layer)
                this.backWeights(ratio);       //Changing weights of neurons
            }
            errorsLog.add(sumError);
        }

        return errorsLog.toArray(new Double[errorsLog.size()]);
    }

    //Learning by following Dataset class
    public void learning(Dataset dataset, float ratio){ this.learning(dataset.getDatasetArray(), ratio); }

    public int[] AIChecker(double[][][] example, int roundRate){   //Compare dataset array and AI output
        int[] resultsInfo = {0, 0};             //resultInfo[0] - dataset length, resultInfo[1] - AI and dataset matches
        for(double[][] dataset : example){
            resultsInfo[0]++;
            double[] result = this.start(dataset[0]);   //Contains AI's output. Will be casted to float[] answer soon
            float[] answer = new float[result.length];
            for(int i = 0; i < answer.length; i++)
                answer[i] = (float) ((float) Math.round(result[i] * Math.pow(10, roundRate)) / Math.pow(10, roundRate));

            //Casting dataset array to float
            float[] exampleOutput = new float[dataset[1].length];
            for(int i = 0; i < dataset[1].length; i++)
                exampleOutput[i] = (float) dataset[1][i];

            /*
                Casting values to float [] is needed to normalize the output and make the output readable
            */
            //Matching dataset array with AI's output
            if(Arrays.equals(answer, exampleOutput))
                resultsInfo[1]++;

            System.out.print("Output: " + Arrays.toString(answer) + " Example: " + Arrays.toString(exampleOutput) + "\n");
        }
        System.out.print(Arrays.toString(resultsInfo) + "\n");

        return resultsInfo;
    }

    //Checking AI by following Dataset class
    public int[] AIChecker(Dataset dataset, int roundRate){ return this.AIChecker(dataset.getDatasetArray(), roundRate); }

    public double[][][] getWeights() {   //Returns a 3D array of the weights(and bias) of all neurons
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

    public void setWeights(double[] weights) {  //Setting weights of neuron's by 1D doubles array
        int index = 0;
        for(int i = 1; i < this.neurons.size(); i++)
            for(int a = 0; a < this.neurons.get(i).size(); a++)
                for(int b = 0; b < this.neurons.get(i).get(a).weights.size() + 1; b++)
                    this.neurons.get(i).get(a).setWeight(b, weights[index++]);
    }

    public void saveAI(String fileName){    //Writes AI(structure, AI functions index, weights) to following filepath
        //Cast three-dimensional [this.dataset] array to one-dimensional array
        double[] weights = Arrays.stream(this.getWeights()).flatMap(Arrays::stream).flatMapToDouble(Arrays::stream).toArray();
        //First element - list size(need to handle data in file), last element - AIFunction id
        double[] AIParameters = new double[this.neurons.size() + 2];
        AIParameters[0] = AIParameters.length - 1;
        for(int i = 1; i < AIParameters.length - 1; i++)
            AIParameters[i] = this.neurons.get(i - 1).size();
        AIParameters[AIParameters.length - 1] = Arrays.asList(AIFunctions.values()).indexOf(this.aiFunctions);

        //Merging [AIParameters] and AI's weights arrays
        double[] result = Arrays.copyOf(AIParameters, weights.length + AIParameters.length);
        System.arraycopy(weights, 0, result, AIParameters.length, weights.length);

        FileHandler.writeToFile(result, fileName);
    }

    //Saving AI without arguments to unique filepath
    public void saveAI(){ this.saveAI(this.toString() + ".bin");}

    //Returns text representation of AI's structure
    public String getAIParameters(){
        int[] output = new int[this.neurons.size()];
        for (int i = 0; i < this.neurons.size(); i++)
            output[i] = this.neurons.get(i).size();
        return Arrays.toString(output);
    }
}