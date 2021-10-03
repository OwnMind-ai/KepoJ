package AILib.layers;

import AILib.utills.AIFunctions;
import AILib.utills.Neuron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClusteredLayer implements Layer{   // !!! ON FREEZE !!!
    public List<Layer> layers;

    public ClusteredLayer(){
        this.layers = new ArrayList<>();
    }

    public ClusteredLayer(Layer... layers){
        this.layers = Arrays.asList(layers);
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
        this.forEachNeuron(data, (layerData, layer) -> layer.doLayer(layerData));

        return this.getOutputs();
    }

    @Override
    public void trainLayer(double[] outputs, float ratio) {
        this.forEachNeuron(outputs, (layerData, layer) -> layer.trainLayer(layerData, ratio));
    }

    @Override
    public void setOutputs(double[] outputs) {
        this.forEachNeuron(outputs, (layerData, layer) -> layer.setOutputs(layerData));
    }

    @Override
    public void findErrors(double[] errors, double[][] weights) {
        for (Layer layer : this.layers)
            layer.findErrors(errors, weights);
    }

    @Override
    public void setErrors(double[] errors) {
        this.forEachNeuron(errors, (data, layer) -> layer.setErrors(data));
    }

    @Override
    public Neuron getNeuron(int index) {
        int startIndex = 0;
        for(Layer layer : layers) {
            if(startIndex <= index && startIndex + layer.getNeuronsLength() > index){
                return layer.getNeuron(index);
            }
            startIndex+= layer.getNeuronsLength();
        }

        return null;
    }

    @Override
    public double[][] getWeights() {
        double[][] result = new double[this.getNeuronsLength()][];
        int counter = 0;

        for (Layer layer : this.layers) {
            System.arraycopy(layer.getWeights(), 0, result, counter, layer.getWeights().length);
            counter+= layer.getNeuronsLength();
        }

        return result;
    }

    @Override
    public double[] getErrors() {
        double[] result = new double[this.getNeuronsLength()];
        int counter = 0;

        for (Layer layer : this.layers) {
            System.arraycopy(layer.getErrors(), 0, result, counter, layer.getErrors().length);
            counter+= layer.getNeuronsLength();
        }

        return result;
    }

    @Override
    public double[] getOutputs() {
        double[] result = new double[this.getNeuronsLength()];
        int counter = 0;

        for (Layer layer : this.layers) {
            System.arraycopy(layer.getOutputs(), 0, result, counter, layer.getOutputs().length);
            counter+= layer.getNeuronsLength();
        }

        return result;
    }

    @Override
    public double[] getBias() {
        double[] result = new double[this.getNeuronsLength()];
        int counter = 0;

        for (Layer layer : this.layers) {
            System.arraycopy(layer.getBias(), 0, result, counter, layer.getOutputs().length);
            counter+= layer.getNeuronsLength();
        }

        return result;
    }

    @Override
    public int getNeuronsLength() {
        int length = 0;
        for(Layer layer : this.layers){
            length+= layer.getNeuronsLength();
        }

        return length;
    }

    @Override
    public double[] getArchivedData() {  // TODO: убрать этот метод и сделать метод который будет возвращать коды для сохранения
        return null;
    }

    private void forEachNeuron(double[] data, ForEachFunction function){
        int indicator = 0;
        for (Layer layer : this.layers) {
            double[] layerData = new double[layer.getNeuronsLength()];
            System.arraycopy(data, indicator, layerData, 0, layerData.length);
            function.run(layerData, layer);

            indicator+= layer.getNeuronsLength();
        }
    }
}

interface ForEachFunction{
    void run(double[] data, Layer layer);
}