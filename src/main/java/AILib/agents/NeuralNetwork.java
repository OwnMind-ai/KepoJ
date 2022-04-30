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

/**
 * Serializable class that provide passing data through a sequence of layers
 * @see Agent
 * @since 1.1
 */
public class NeuralNetwork implements Agent, Serializable {
    protected ArrayList<Layer> layers;
    /**
     * Permissible error during training
     */
    public double fault = 0.0005d;

    /**
     * @param inputNeurons input layer length
     * @since 1.1
     */
    public NeuralNetwork(int inputNeurons){
        this.layers = new ArrayList<>();
        this.layers.add(new InputLayer(inputNeurons));
    }

    /**
     * Read serializable neural network from file
     * @param fileName path to file
     * @since 1.1
     */
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

    /**
     * Adds layer to end of list
     * @param layer instance of Layer
     * @since 1.1
     */
    public void addLayer(Layer layer) {
        this.layers.add(layer);
        this.layers.get(this.layers.size() - 1).buildLayer(
                this.layers.get(this.layers.size() - 2).length()
        );
    }

    /**
     * Adds few layers at once
     * @param layers layers array
     * @since 1.1
     */
    public void addAll(Layer... layers){
        for(Layer layer : layers)
            this.addLayer(layer);
    }

    /**
     * Runs input data through layers successively
     * @param inputData input data
     * @return output values on last layer
     * @throws NeuralNetworkRuntimeException throws if length of input data and input layer not the same
     * @since 1.1
     */
    @Override
    public double[] react(double... inputData){
        if(inputData.length != this.layers.get(0).length()){
            throw new NeuralNetworkRuntimeException("Invalid input data for NeuralNetwork: " + inputData.length);
        }
        this.layers.get(0).setOutputs(inputData);

        for(int i = 1; i < this.layers.size(); i++)
            this.layers.get(i).doLayer(this.layers.get(i - 1).getOutputs());

        return this.layers.get(this.layers.size() - 1).getOutputs();
    }

    /**
     * @return length of last layer
     * @since 1.2
     */
    @Override
    public int outputLength() {
        return this.layers.get(this.layers.size() - 1).length();
    }

    /**
     * Returns 3d-array of neuron weights.
     * First dimension - layers.
     * Second dimension - neurons.
     * Third dimension - weights with bias value at the end.
     * @see NeuralNetwork#setWeights(double[])
     * @deprecated
     * @since 1.1
     */
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

    /**
     * Sets 3d-array weights into neurons.
     * First dimension - layers
     * Second dimension - neurons
     * Third dimension - weights with bias value at the end
     * @see NeuralNetwork#getWeights()
     * @deprecated
     * @since 1.1
     */
    @Deprecated
    public void setWeights(double[] weights) {
        int index = 0;
        for(int i = 1; i < this.layers.size(); i++)
            for(int a = 0; a < this.layers.get(i).length(); a++)
                for(int b = 0; b < this.layers.get(i).getNeuron(a).weights.size() + 1; b++)
                    this.layers.get(i).getNeuron(a).setWeight(b, weights[index++]);
    }

    /**
     * Writes neural network class to provided file
     * @param fileName path to file
     * @since 1.1
     */
    public void save(String fileName) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)));
        stream.writeObject(this);
        stream.close();
    }

    /**
     * Writes neural network to file named by hashcode
     * @since 1.1
     */
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
