package ai.engine.kepoj.layers;

import ai.engine.kepoj.entities.Neuron;
import ai.engine.kepoj.functions.ActivationFunction;
import ai.engine.kepoj.functions.StandardFunctions;

/**
 * Basic layer implementation with fixed neurons amount. Requires less memory space than
 * thr DynamicLayer class because it uses an array instead of a list
 * @see Layer
 * @see DynamicLayer
 * @since 1.1
 */
public class StaticLayer extends StaticLayerBase {
    /**
     * Creates a layer by a fixed number of neurons
     * @param neuronCount number of neurons in a layer
     * @param functions activation function complex for this layer
     * @since 1.1
     */
    public StaticLayer(int neuronCount, ActivationFunction functions){
        super(functions);
        this.neurons = new Neuron[neuronCount];
    }

    /**
     * Creates a layer by a fixed number of neurons
     * @param neuronsCount number of neurons in a layer
     * @param function defined function from the StandardFunction enum
     * @since 1.1
     */
    public StaticLayer(int neuronsCount, StandardFunctions function){
        this(neuronsCount, function.get());
    }

    /**
     * Builds a layer structure based on the previous layer. Usually, Layer must be built in the neural network class.
     *
     * @param previous layer object before current layer
     * @since 1.1
     */
    @Override
    public void buildLayer(Layer previous) {
        assert previous != null : "Previous layer is null";
        for (int i = 0; i < this.neurons.length; i++)
            this.neurons[i] = new Neuron(previous.length(), this.aiFunctions);
    }
}
