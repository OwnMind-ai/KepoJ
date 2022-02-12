package AILib.entities;

import static org.junit.jupiter.api.Assertions.*;

class NeuronTest {

    @org.junit.jupiter.api.Test
    void doNeuron() {
        Neuron neuron;

        neuron = new Neuron(new double[]{1,1,1,1,1}, AIFunctions.IDENTICAL);
        assertEquals(9, neuron.excite(new double[]{1, 3, 5, -1}));

        neuron = new Neuron(new double[]{-1,-1,-1,-1,1}, AIFunctions.IDENTICAL);
        assertEquals(-7, neuron.excite(new double[]{1, 3, 5, -1}));

        neuron = new Neuron(new double[]{1}, AIFunctions.IDENTICAL);
        assertEquals(1, neuron.excite(new double[]{}));
    }
}