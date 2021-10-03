package AILib.agents;

import AILib.layers.InputLayer;
import AILib.layers.Layer;
import AILib.layers.Layers;
import AILib.layers.StaticLayer;
import AILib.utills.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AI implements Agent{
    public static double VERSION = 1.1d;
    public static double LAYER_SPLITTER = -256;
    public static double WEIGHTS_START = -257;

    protected ArrayList<Layer> layers;     //Dynamic array of Neurons classes
    public float fault = 0.0005f;          //Minimal error of neural network1

    public AI(int inputNeurons){    //Initialization by pre-creating first layer         //Dynamic ID
        buildAI(inputNeurons);
    }

    public AI(String fileName){                          //Initialization by import of existing AI
        double[] AIParameters = FileHandler.readFile(fileName);       //AI's file data
        assert(AIParameters != null);
        this.layers = new ArrayList<>();

        int i = 2;
        if(AIParameters[i] == Arrays.asList(Layers.values()).indexOf(Layers.INPUT_LAYER)){
            this.layers.add(
                    Layers.values()[(int) AIParameters[i]].getInstance(
                        new double[]{AIParameters[i + 1]}
            ));
            i+= 2;
        }
        while(AIParameters[i] != AI.WEIGHTS_START){
            //Initialization layers
            if(AIParameters[i] == AI.LAYER_SPLITTER){
                double[] data = new double[Layers.values()[(int) AIParameters[i + 1]].getDataLength()];
                System.arraycopy(AIParameters, i + 2, data, 0, data.length);
                this.addLayer(
                        Layers.values()[(int) AIParameters[i + 1]].getInstance(data)
                );
            }
            i+= 1;
        }

        double[] weights = new double[(int) (AIParameters.length - i - 1)];
        System.arraycopy(AIParameters, i + 1, weights, 0, weights.length);
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

    public void save(String fileName){    //Writes AI(structure, weights) to following filepath
        //Cast three-dimensional [this.dataset] array to one-dimensional array
        double[] weights = Arrays.stream(this.getWeights())
                .flatMap(Arrays::stream).flatMapToDouble(Arrays::stream).toArray();
        ArrayList<Double> AIParametersList = new ArrayList<>();
        AIParametersList.add(AI.VERSION);
        for (Layer layer : this.layers) {
            AIParametersList.add(AI.LAYER_SPLITTER);

            AIParametersList.add((double) Layers.getLayerID(layer.getClass()));
            double[] data = layer.getArchivedData();
            for (double d : data)
                AIParametersList.add(d);
        }
        AIParametersList.add(AI.WEIGHTS_START);

        double[] AIParameters = AIParametersList.stream().mapToDouble(Double::doubleValue).toArray();
        //Merging [AIParameters] and AI's weights arrays
        double[] result = Arrays.copyOf(AIParameters, weights.length + AIParameters.length);
        System.arraycopy(weights, 0, result, AIParameters.length, weights.length);

        FileHandler.writeToFile(result, fileName);
    }

    //Saving AI without arguments to unique filepath
    public void save(){ this.save(this.toString() + ".bin");}
}