package ai.engine.kepoj.layers;

import ai.engine.kepoj.functions.ActivationFunction;
import ai.engine.kepoj.functions.StandardFunctions;
import ai.engine.kepoj.entities.Neuron;

public class StaticLayer implements Layer {
    private final ActivationFunction aiFunctions;
    private final Neuron[] neurons;
    
    public StaticLayer(int neuronCount, ActivationFunction functions){
        this.aiFunctions = functions;
        this.neurons = new Neuron[neuronCount];
    }

    public StaticLayer(int neuronsCount, StandardFunctions function){
        this(neuronsCount, function.get());
    }

    public void buildLayer(Layer previous){
        assert previous != null : "Previous layer is null";
        for (int i = 0; i < this.neurons.length; i++)
            this.neurons[i] = new Neuron(previous.length(), this.aiFunctions);
    }

    @Override
    public void doLayer(double[] data) {
        for(Neuron neuron : this.neurons)
            neuron.excite(data);
    }

    @Override
    public double[] getOutputs() {    // TODO: optimize
        double[] result = new double[this.neurons.length];
        for(int i = 0; i < this.neurons.length; i++)
            result[i] = this.neurons[i].output;

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
    public double[][] getWeights() {
        double[][] result = new double[this.neurons.length][];
        for(int i = 0; i < this.neurons.length; i++)
            result[i] = this.neurons[i].weights.stream().mapToDouble(Double::doubleValue).toArray();

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
    public int length() {
        return this.neurons.length;
    }

    @Override
    public Neuron getNeuron(int index) {
        return this.neurons[index];
    }

    @Override
    public void findErrors(double[] errors, double[][] weights) {
        for(int i = 0; i < this.neurons.length; i++) {
            double error = 0;
            for(int j = 0; j < errors.length;j++)
                error+= errors[j] * weights[j][i];

            this.neurons[i].setError(error);
        }
    }

    @Override
    public void trainLayer(double[] outputs, double ratio) {
        for (Neuron neuron : this.neurons) {
            for (int j = 0; j < neuron.weights.size(); j++)
                neuron.weights.set(j,
                        neuron.weights.get(j) + ratio * outputs[j] * neuron.error
                );
            neuron.bias += ratio * neuron.error;
        }
    }

    @Override
    public void setOutputs(double[] outputs) {
        for(int i = 0; i < this.neurons.length; i++)
            this.neurons[i].output = outputs[i];
    }
}
