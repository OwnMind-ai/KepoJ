package AILib.agents;

import AILib.entities.Dataset;
import AILib.layers.InputLayer;
import AILib.layers.Layer;
import AILib.layers.Layers;
import AILib.utills.*;

import java.util.ArrayList;
import java.util.Arrays;

/*
* NeuralNetwork is base class to neural network agents,
* has neural structure and can 
*/

public class NeuralNetwork implements Agent{
    public static double VERSION = 1.1d;         // Version of saving format, not used in program
    public static double LAYER_SPLITTER = -256;  // Code of start layer's structure description
    public static double WEIGHTS_START = -257;   // Code of start neural network weights enumeration

    protected ArrayList<Layer> layers;     // Dynamic array of Neurons classes
    public float fault = 0.0005f;          // Minimal error of neural network

    public NeuralNetwork(int inputNeurons){    //Initialization by pre-creating first layer
        buildAI(inputNeurons);
    }

    public NeuralNetwork(String fileName){                          //Initialization by import Agent save file
        double[] parameters = FileHandler.readFile(fileName);       // Gets data from save file
        assert(parameters != null);
        this.layers = new ArrayList<>();

        int i = 2;          // Starts from first Layer index(skip version and layer splitter)
        // This code in if statement add InputLayer to neural network without calling Layer::buildLayer
        if(parameters[i] == Arrays.asList(Layers.values()).indexOf(Layers.INPUT_LAYER)){
            this.layers.add(
                    Layers.values()[(int) parameters[i]].getInstance(
                        new double[]{parameters[i + 1]}
            ));
            i+= 2;   // Set index at next layer splitter
        }
        while(parameters[i] != NeuralNetwork.WEIGHTS_START){
            // Initialization layers
            if(parameters[i] == NeuralNetwork.LAYER_SPLITTER){
                // Gets layer data after layer index
                double[] data = new double[Layers.values()[(int) parameters[i + 1]].getDataLength()];
                System.arraycopy(parameters, i + 2, data, 0, data.length);
                // Adds layer by getting instance from Layers Enum
                this.addLayer(
                        Layers.values()[(int) parameters[i + 1]].getInstance(data)
                );
            }
            i+= 1;
        }

        // Getting weights from parameters
        double[] weights = new double[(int) (parameters.length - i - 1)];
        System.arraycopy(parameters, i + 1, weights, 0, weights.length);
        this.setWeights(weights);     //Setting weights from file data
    }

    private void buildAI(int neuronsCount){    // Building Agent base structure
        this.layers = new ArrayList<>();
        this.layers.add(new InputLayer(neuronsCount));
    }

    public void setFault(float fault) {
        this.fault = fault;
    }

    public void addLayer(Layer layer) {              // Adds layer to [this.layers] array and adds neurons to it
        this.layers.add(layer);
        // Builds layer by giving information of previous layers
        this.layers.get(this.layers.size() - 1).buildLayer(
                this.layers.get(this.layers.size() - 2).getNeuronsLength()
        );
    }

    public void addAll(Layer... layers){    // Adds several layers at once
        for(Layer layer : layers)
            this.addLayer(layer);
    }

    public double[] start(double[] inputData){      // Runs the input array through neural network and returns output
        this.layers.get(0).setOutputs(inputData);   // Loads data to first layer

        for(int i = 1; i < this.layers.size(); i++)
            // Does layer by giving result on previous layer
            this.layers.get(i).doLayer(this.layers.get(i - 1).getOutputs());

        // Returns outputs of last layer as Agent output
        return this.layers.get(this.layers.size() - 1).getOutputs();
    }

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

        ArrayList<Double> parametersList = new ArrayList<>();  // List of parameters that will saved
        parametersList.add(NeuralNetwork.VERSION);                        // Adds saving format version
        for (Layer layer : this.layers) {
            // Adds splitter and starts write Layer structure to list
            parametersList.add(NeuralNetwork.LAYER_SPLITTER);

            // Adds index of layer from Layers Enum
            parametersList.add((double) Layers.getLayerID(layer.getClass()));
            double[] data = layer.getArchivedData();   // Gets data of layer structure
            for (double d : data)
                parametersList.add(d);
        }
        // Starts to adding 1D weights array to list
        parametersList.add(NeuralNetwork.WEIGHTS_START);

        // Cast parameters list to doubles array
        double[] parameters = parametersList.stream().mapToDouble(Double::doubleValue).toArray();
        //Merging [parameters] and AI's weights arrays
        double[] result = Arrays.copyOf(parameters, weights.length + parameters.length);
        System.arraycopy(weights, 0, result, parameters.length, weights.length);

        FileHandler.writeToFile(result, fileName);  // Writes Agent data to file
    }

    //Saving AI without arguments to unique filepath
    public void save(){ this.save(this.toString() + ".bin");}
}