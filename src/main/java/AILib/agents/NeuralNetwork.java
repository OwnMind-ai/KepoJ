package AILib.agents;

import AILib.exceptions.NeuralNetworkRuntimeException;
import AILib.layers.InputLayer;
import AILib.layers.Layer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class NeuralNetwork implements Agent, Serializable {
    protected ArrayList<Layer> layers;
    public double fault = 0.0005d;

    public NeuralNetwork(int inputNeurons){    //Initialization by pre-creating first layer
        this.layers = new ArrayList<>();
        this.layers.add(new InputLayer(inputNeurons));
    }

    public NeuralNetwork(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
        NeuralNetwork result = (NeuralNetwork) stream.readObject();
        stream.close();

        this.layers = new ArrayList<>(result.layers);
        this.fault = result.fault;
    }

    @Deprecated
    public void setFault(double fault) {
        this.fault = fault;
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
        this.layers.get(this.layers.size() - 1).buildLayer(
                this.layers.get(this.layers.size() - 2).length()
        );
    }

    public void addAll(Layer... layers){
        for(Layer layer : layers)
            this.addLayer(layer);
    }

    public double[] react(double... inputData){
        assert inputData.length == this.layers.get(0).length() :
                new NeuralNetworkRuntimeException("Invalid input data for NeuralNetwork: " + inputData.length);
        this.layers.get(0).setOutputs(inputData);

        for(int i = 1; i < this.layers.size(); i++)
            this.layers.get(i).doLayer(this.layers.get(i - 1).getOutputs());

        return this.layers.get(this.layers.size() - 1).getOutputs();
    }

    @Override
    public int outputLength() {
        return this.layers.get(this.layers.size() - 1).length();
    }

    @Deprecated
    public double[][][] getWeights() {
        double[][][] weights = new double[this.layers.size() - 1][][];
        for(int i = 1; i < this.layers.size(); i++) {
            weights[i - 1] = this.layers.get(i).getWeights();
            for(int j = 0; j < this.layers.get(i).length(); j++) {
                weights[i - 1][j] = Arrays.copyOf(weights[i - 1][j], weights[i - 1][j].length + 1);
                weights[i - 1][j][weights[i - 1][j].length - 1] = this.layers.get(i).getBias()[j];
            }
        }

        return weights;
    }

    @Deprecated
    public void setWeights(double[] weights) {
        int index = 0;
        for(int i = 1; i < this.layers.size(); i++)
            for(int a = 0; a < this.layers.get(i).length(); a++)
                for(int b = 0; b < this.layers.get(i).getNeuron(a).weights.size() + 1; b++)
                    this.layers.get(i).getNeuron(a).setWeight(b, weights[index++]);
    }

    public void save(String fileName) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)));
        stream.writeObject(this);
        stream.close();
    }

    public void save() throws IOException { this.save(this.hashCode() + ".bin");}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeuralNetwork network = (NeuralNetwork) o;
        return layers.equals(network.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layers);
    }
}
