package AILib.agents;

import AILib.functions.ActivationFunction;
import AILib.layers.InputLayer;
import AILib.layers.Layer;
import AILib.layers.Layers;
import AILib.utills.FileHandler;
import AILib.utills.NeuralNetworkReader;

import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetwork implements Agent{
    protected ArrayList<Layer> layers;
    public double fault = 0.0005d;

    public NeuralNetwork(int inputNeurons){    //Initialization by pre-creating first layer
        buildAI(inputNeurons);
    }

    public NeuralNetwork(String fileName){

    }


    private void buildAI(int neuronsCount){
        this.layers = new ArrayList<>();
        this.layers.add(new InputLayer(neuronsCount));
    }

    public void setFault(double fault) {
        this.fault = fault;
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
        this.layers.get(this.layers.size() - 1).buildLayer(
                this.layers.get(this.layers.size() - 2).getNeuronsLength()
        );
    }

    public void addAll(Layer... layers){
        for(Layer layer : layers)
            this.addLayer(layer);
    }

    public double[] react(double[] inputData){
        this.layers.get(0).setOutputs(inputData);

        for(int i = 1; i < this.layers.size(); i++)
            this.layers.get(i).doLayer(this.layers.get(i - 1).getOutputs());

        return this.layers.get(this.layers.size() - 1).getOutputs();
    }

    public double[][][] getWeights() {
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

    public void setWeights(double[] weights) {
        int index = 0;
        for(int i = 1; i < this.layers.size(); i++)
            for(int a = 0; a < this.layers.get(i).getWeights().length; a++)
                for(int b = 0; b < this.layers.get(i).getNeuron(a).weights.size() + 1; b++)
                    this.layers.get(i).getNeuron(a).setWeight(b, weights[index++]);
    }

    public void save(String fileName){
        double[] weights = Arrays.stream(this.getWeights())
                .flatMap(Arrays::stream).flatMapToDouble(Arrays::stream).toArray();

        ArrayList<Double> parametersList = new ArrayList<>();
        parametersList.add((double) NeuralNetworkReader.VERSION);
        for (Layer layer : this.layers) {
            parametersList.add((double) NeuralNetworkReader.LAYER_SPLITTER);

            parametersList.add((double) Layers.getLayerID(layer.getClass()));
            double[] data = layer.getArchivedData();
            for (double d : data)
                parametersList.add(d);
        }

        parametersList.add((double) NeuralNetworkReader.WEIGHTS_START);

        double[] parameters = parametersList.stream().mapToDouble(Double::doubleValue).toArray();
        double[] result = Arrays.copyOf(parameters, weights.length + parameters.length);
        System.arraycopy(weights, 0, result, parameters.length, weights.length);

        FileHandler.writeToFile(result, fileName);
    }

    public void save(){ this.save(this.toString() + ".bin");}

    public native int sum(int a, int b);
}
