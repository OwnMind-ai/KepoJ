package AILib.layers;

import AILib.utills.AIFunctions;
import AILib.utills.Neuron;

import java.util.ArrayList;
import java.util.Arrays;

public class ClusteredLayer implements Layer{
    public ArrayList<Layer> layers;

    public ClusteredLayer(){
        this.layers = new ArrayList<>();
    }

    public ClusteredLayer(Layer... layers){
        this.layers = (ArrayList<Layer>) Arrays.asList(layers);
    }

    public void addLayer(Layer layer){
        this.layers.add(layer);
    }

    @Override
    public void buildLayer(int weightsCount) {
        layers.forEach(layer -> layer.buildLayer(weightsCount));
    }

    @Override
    public double[] doLayer(double[] data) {
        /*int indicator = 0;
        for (int i = 0; i < this.layers.size(); i++) {
            this.layers.get(i).doLayer(data.);
        }*/
        return new double[0];
    }

    @Override
    public void trainLayer(double[] outputs, float ratio) {

    }

    @Override
    public void setOutputs(double[] outputs) {

    }

    @Override
    public void findErrors(double[] errors, double[][] weights) {

    }

    @Override
    public void setError(double[] dataset) {

    }

    @Override
    public Neuron getNeuron(int index) {
        return null;
    }

    @Override
    public double[][] getWeights() {
        return new double[0][];
    }

    @Override
    public double[] getErrors() {
        return new double[0];
    }

    @Override
    public double[] getOutputs() {
        return new double[0];
    }

    @Override
    public double[] getBias() {
        return new double[0];
    }

    @Override
    public int getNeuronsLength() {
        return 0;
    }

    @Override
    public AIFunctions getAIFunction() {
        return null;
    }
}
