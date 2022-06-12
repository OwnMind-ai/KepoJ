package AILib.layers;

import AILib.entities.Neuron;
import AILib.functions.ActivationFunction;
import AILib.functions.StandardFunctions;

import java.util.ArrayList;

public class DynamicLayer implements Layer{
    private final ActivationFunction aiFunctions;
    private final ArrayList<Neuron> neurons;
    private final int initialSize;
    private final Layer nextLayer;
    private int weightsCount;

    public DynamicLayer(int initialSize, ActivationFunction functions, Layer nextLayer){
        this.aiFunctions = functions;
        
        this.neurons = new ArrayList<>();
        this.initialSize = initialSize;
        this.nextLayer = nextLayer;
    }

    public DynamicLayer(int initialSize, StandardFunctions function, Layer nextLayer){
        this(initialSize, function.get(), nextLayer);
    }

    public void add(Neuron neuron, double[] weights){
        this.neurons.add(neuron);

        for (int i = 0; i < this.nextLayer.length(); i++)
            this.nextLayer.getNeuron(i).weights.add(weights[i]);
    }

    public void addNeuron(Neuron neuron){
        this.neurons.add(neuron);

        for (int i = 0; i < this.nextLayer.length(); i++)
            this.nextLayer.getNeuron(i).weights.add(Neuron.random());
    }

    public void removeNeuron(int index){
        this.neurons.remove(index);

        for (int i = 0; i < this.nextLayer.length(); i++)
            this.nextLayer.getNeuron(i).weights.remove(index);
    }

    @Override
    public void buildLayer(double... data){
        assert data.length > 0 : "Build layer data is empty";

        for (int i = 0; i < initialSize; i++)
            this.neurons.add(new Neuron((int) data[0], this.aiFunctions));
    }

    @Override
    public void doLayer(double[] data) {
        for(Neuron neuron : this.neurons)
            neuron.excite(data);
    }

    @Override
    public double[] getOutputs() {
        return this.neurons.stream().mapToDouble(x -> x.output).toArray();
    }

    @Override
    public double[] getErrors() {
        return this.neurons.stream().mapToDouble(x -> x.error).toArray();
    }

    @Override
    public double[][] getWeights() {
        double[][] result = new double[this.neurons.size()][];
        for(int i = 0; i < this.neurons.size(); i++)
            result[i] = this.neurons.get(i).weights.stream().mapToDouble(Double::doubleValue).toArray();

        return result;
    }

    @Override
    public double[] getBias() {
        return this.neurons.stream().mapToDouble(x -> x.bias).toArray();
    }

    @Override
    public int length() {
        return this.neurons.size();
    }

    @Override
    public void setErrors(double[] errors) {
        for(int i = 0; i < this.neurons.size(); i++)
            this.neurons.get(i).setError(errors[i]);
    }

    @Override
    public Neuron getNeuron(int index) {
        return this.neurons.get(index);
    }

    @Override
    public void findErrors(double[] errors, double[][] weights) {
        for(int i = 0; i < this.neurons.size(); i++) {
            double error = 0;
            for(int j = 0; j < errors.length;j++)
                error+= errors[j] * weights[j][i];

            this.neurons.get(i).setError(error);
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
        for(int i = 0; i < this.neurons.size(); i++)
            this.neurons.get(i).output = outputs[i];
    }
}
