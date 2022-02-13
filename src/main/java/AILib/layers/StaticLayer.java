package AILib.layers;

import AILib.entities.Neuron;
import AILib.functions.ActivationFunction;
import AILib.functions.StandardFunctions;

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

    public void buildLayer(int weightsCount){
        for (int i = 0; i < this.neurons.length; i++)
            this.neurons[i] = new Neuron(weightsCount, this.aiFunctions);
    }

    /*
    * data[0] - neurons length
    * data[1] - AIFunction index
    */

    @Override
    public double[] getArchivedData() {
        return null; /* new double[]{
                this.getNeuronsLength(),
                Arrays.asList(ActivationFunction.values())
                        .indexOf(this.aiFunctions)
        }; */
    }

    @Override
    public double[] doLayer(double[] data) {
        for(Neuron neuron : this.neurons) {
            neuron.excite(data);
        }

        return this.getOutputs();
    }

    @Override
    public double[] getOutputs() {
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
    public int getNeuronsLength() {
        return this.neurons.length;
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
