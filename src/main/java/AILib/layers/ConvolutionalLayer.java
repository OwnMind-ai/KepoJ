package AILib.layers;

import AILib.entities.AIFunctions;
import AILib.entities.Neuron;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class ConvolutionalLayer implements Layer {
    private final AIFunctions aiFunctions;
    private final Neuron[] neurons;

    public final int layerSizeX;
    public final int layerSizeY;
    private final int previousLayerX;
    private final int previousLayerY;
    private final int coreSizeX;
    private final int coreSizeY;

    public ConvolutionalLayer(int coreSizeX, int coreSizeY,
                              int previousLayerX, int previousLayerY,
                              AIFunctions aiFunctions){
        this.aiFunctions = aiFunctions;

        this.coreSizeX = coreSizeX;
        this.coreSizeY = coreSizeY;
        this.previousLayerX = previousLayerX;
        this.previousLayerY = previousLayerY;
        this.layerSizeX = ConvolutionalLayer.calculateLayersSize(coreSizeX, previousLayerX);
        this.layerSizeY = ConvolutionalLayer.calculateLayersSize(coreSizeY, previousLayerY);

        this.neurons = new Neuron[this.layerSizeX * this.layerSizeY];
    }

    private static int calculateLayersSize(int coreSize, int previousLayerSize){
        return previousLayerSize - coreSize + 1;
    }

    private int getIdByPosition(int x, int y){
        return y * this.previousLayerX + x;
    }

    public double[] calculateWeights(int corePositionX, int corePositionY){
        double[] weights = new double[this.previousLayerX * this.previousLayerY];

        for(int y = corePositionY; y < this.coreSizeY + corePositionY; y++){
            for(int x = corePositionX; x < this.coreSizeX + corePositionX; x++){
                weights[this.getIdByPosition(x, y)] = new Random().nextDouble();
            }
        }

        return weights;
    }

    @Override
    public void buildLayer(int weightsCount) {
        for(int i = 0; i < this.neurons.length; i++){
            this.neurons[i] = new Neuron(
                this.calculateWeights(
                        i % this.layerSizeX,
                        (int) Math.floor((double) i / this.layerSizeY)),
                this.aiFunctions
            );
        }
    }

    /*
     * data[0] and data[1] - core size
     * data[2] and data[3] - previous layer size
     * data[4] - AIFunction index
     */

    @Override
    public double[] getArchivedData() {
        return new double[]{
                this.coreSizeX,
                this.coreSizeY,
                this.previousLayerX,
                this.previousLayerY,
                Arrays.asList(AIFunctions.values())
                        .indexOf(this.aiFunctions)
        };
    }

    @Override
    public double[] doLayer(double[] data) {
        for(Neuron neuron : this.neurons) {
            neuron.excite(data);
        }

        return this.getOutputs();
    }

    @Override
    public void trainLayer(double[] outputs, float ratio) {
        for (Neuron neuron : this.neurons) {
            for (int j = 0; j < neuron.weights.size(); j++)
                if (neuron.weights.get(j) != null) {
                    neuron.weights.set(j,
                            neuron.weights.get(j) + ratio * outputs[j] * neuron.error
                    );
                }
            neuron.bias += ratio * neuron.error;
        }
    }

    @Override
    public void setOutputs(double[] outputs) {
        for(int i = 0; i < this.neurons.length; i++)
            this.neurons[i].output = outputs[i];
    }

    @Override
    public void findErrors(double[] errors, double[][] weights) {
        for(int i = 0; i < this.neurons.length; i++) {
            double error = 0;
            for(int j = 0; j < errors.length;j++)
                error+= errors[j] * (Collections.singletonList(weights[j][i]).get(0) == null ? 0 : weights[j][i]);

            this.neurons[i].setError(error);
        }
    }

    @Override
    public void setErrors(double[] errors) {
        for(int i = 0; i < this.neurons.length; i++)
            this.neurons[i].setError(errors[i]);
    }

    @Override
    public Neuron getNeuron(int index) {
        return this.neurons[index];
    }

    @Override
    public double[][] getWeights() {
        double[][] result = new double[this.neurons.length][];
        for(int i = 0; i < this.neurons.length; i++)
            result[i] = this.neurons[i].weights.stream().mapToDouble(Double::doubleValue).toArray();

        return result;
    }

    @Override
    public double[] getErrors() {
        double[] result = new double[this.neurons.length];
        for(int i = 0; i < this.neurons.length; i++)
            result[i] = this.neurons[i].error;

        return result;
    }

    @Override
    public double[] getOutputs() {
        double[] result = new double[this.neurons.length];
        for(int i = 0; i < this.neurons.length; i++)
            result[i] = this.neurons[i].output;

        return result;
    }

    @Override
    public double[] getBias() {
        double[] result = new double[this.neurons.length];
        for(int i = 0; i < this.neurons.length; i++)
            result[i] = this.neurons[i].bias;

        return result;
    }

    @Override
    public int getNeuronsLength() {
        return this.neurons.length;
    }
}
