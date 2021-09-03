package AILib;

import AILib.layers.InputLayer;
import AILib.layers.Layer;
import AILib.layers.StaticLayer;
import AILib.utills.*;

import java.util.ArrayList;
import java.util.Arrays;

public class AI{
    public ArrayList<Layer> layers;     //Dynamic array of Neurons classes
    public float fault = 0.0005f;          //Minimal error of neural network1

    public AI(int inputNeurons){    //Initialization by pre-creating first layer         //Dynamic ID
        buildAI(inputNeurons);
    }

    public AI(String fileName){                          //Initialization by import of existing AI
        double[] AIParameters = FileHandler.readFile(fileName);       //AI's file data

        assert(AIParameters != null);
        this.buildAI((int) AIParameters[1]);
        for(int i = 2; i < (int) AIParameters[0]; i+= 2) {
            //Initialization layers
            Layer layer = AIParameters[i] >= 0 ?
                    new StaticLayer((int) Math.abs(AIParameters[i]), AIFunctions.values()[(int) AIParameters[i + 1]]) :
                    null;  //Dynamic layers will added soon
            this.addLayer(layer);
        }

        double[] weights = new double[(int) (AIParameters.length - AIParameters[0] - 1)];
        System.arraycopy(AIParameters, (int) AIParameters[0] + 1, weights, 0, weights.length);
        this.setWeights(weights);     //Setting weights from file data
    }

    private void buildAI(int neuronsCount){    //Building AI structure
        this.layers = new ArrayList<>();
        this.layers.add(new InputLayer(neuronsCount));
    }

    public void setFault(float fault) {
        this.fault = fault;
    }

    public void addLayer(Layer layer) {              //Adds layer to [this.layers] array and adds neurons to it
        this.layers.add(layer);                     //Adding layer
        this.layers.get(this.layers.size() - 1).buildLayer(
                this.layers.get(this.layers.size() - 2).getNeuronsLength()
        );
    }

    public void addAll(Layer... layers){
        for(Layer layer : layers)
            this.addLayer(layer);
    }

    public double[] start(double[] inputData){      //Runs the input array through neural network and returns output
        this.layers.get(0).setOutputs(inputData);

        for(int i = 1; i < this.layers.size(); i++)
            this.layers.get(i).doLayer(this.layers.get(i - 1).getOutputs());

        return this.layers.get(this.layers.size() - 1).getOutputs();
    }

    private void findError(){               //Calculates error of all neurons(without output layer)
        for(int i = this.layers.size() - 2; i > 0; i--){
            this.layers.get(i).findErrors(
                    this.layers.get(i + 1).getErrors(),
                    this.layers.get(i + 1).getWeights()
            );
        }
    }

    private void backWeights(float ratio) {    //Changing weights of neurons. Ratio - learning coefficient
        for(int i = 1; i < this.layers.size(); i++)
           this.layers.get(i).trainLayer(this.layers.get(i - 1).getOutputs(), ratio);
    }

    public double[] learning(double[][][] example, float ratio){  //Trains AI by following dataset array and learning ratio
        ArrayList<Double> errorsLog = new ArrayList<>();
        double sumError = Double.MAX_VALUE;     //(setting to maximum value for first while iteration)
        int age = 0;

        while (sumError >= this.fault) {    //Training continues until the error is less than the minimum([this.fault])
            sumError = 0;
            for (double[][] data : example) {
                //Calculating current error by example
                double[] result = this.start(data[0]);
                for (int a = 0; a < result.length; a++)
                    sumError += Math.pow((data[1][a] - result[a]), 2);

                this.layers.get(this.layers.size() - 1).datasetOffsetError(data[1]);  //Calculates error of output layer
                this.findError();              //Calculates error of all neurons(without output layer)
                this.backWeights(ratio);       //Changing weights of neurons
            }
            errorsLog.add(sumError);
            if(age % 100 == 0) this.saveAI("AI2.ai");
            System.out.println(age + " - " + sumError);
            age++;
        }

        return errorsLog.stream().mapToDouble(Double::doubleValue).toArray();
    }

    //Learning by following Dataset class
    public double[] learning(Dataset dataset, float ratio){ return this.learning(dataset.getDatasetArray(), ratio); }

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
        double[][][] weights = new double[this.layers.size() - 1][][];
        for(int i = 1; i < this.layers.size(); i++) {
            weights[i - 1] = this.layers.get(i).getWeights();
            for(int j = 0; j < this.layers.get(i).getBias().length; j++) {
                weights[i - 1][j] = Arrays.copyOf(weights[i - 1][j], weights[i - 1][j].length + 1);
                weights[i - 1][j][weights[i - 1][j].length - 1] = this.layers.get(i).getBias()[j];
            }
        }

        return weights;
    }

    public void setWeights(double[] weights) {  //Setting weights of neuron's by 1D doubles array
        int index = 0;
        for(int i = 1; i < this.layers.size(); i++)
            for(int a = 0; a < this.layers.get(i).getWeights().length; a++)
                for(int b = 0; b < this.layers.get(i).getNeuron(a).weights.size() + 1; b++)
                    this.layers.get(i).getNeuron(a).setWeight(b, weights[index++]);
    }

    public void saveAI(String fileName){    //Writes AI(structure, AI functions index, weights) to following filepath
        //Cast three-dimensional [this.dataset] array to one-dimensional array
        double[] weights = Arrays.stream(this.getWeights())
                .flatMap(Arrays::stream).flatMapToDouble(Arrays::stream).toArray();
        //First element - list size(need to handle data in file), last element - AIFunction id
        ArrayList<Double> AIParametersList = new ArrayList<>();
        AIParametersList.add((double) (this.layers.size() * 2 - 1));
        AIParametersList.add((double) this.layers.get(0).getWeights().length);
        for(int i = 1; i < this.layers.size(); i++) {
            AIParametersList.add((double) this.layers.get(i).getWeights().length);
            AIParametersList.add((double) Arrays.asList(AIFunctions.values())
                    .indexOf(this.layers.get(i).getAIFunction()));
        }
        double[] AIParameters = AIParametersList.stream().mapToDouble(Double::doubleValue).toArray();
        //Merging [AIParameters] and AI's weights arrays
        double[] result = Arrays.copyOf(AIParameters, weights.length + AIParameters.length);
        System.arraycopy(weights, 0, result, AIParameters.length, weights.length);

        FileHandler.writeToFile(result, fileName);
    }

    //Saving AI without arguments to unique filepath
    public void saveAI(){ this.saveAI(this.toString() + ".bin");}
}