package ai.engine.kepoj.entities;

import ai.engine.kepoj.functions.StandardFunctions;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NeuronTest {

    @org.junit.jupiter.api.Test
    void excite() {
        Neuron neuron;
        neuron = new Neuron(new double[]{1,1,1,1}, 1, StandardFunctions.IDENTICAL.get());
        assertEquals(9, neuron.excite(new double[]{1, 3, 5, -1}));

        neuron = new Neuron(new double[]{-1,-1,-1,-1}, 1, StandardFunctions.IDENTICAL.get());
        assertEquals(-7, neuron.excite(new double[]{1, 3, 5, -1}));

        neuron = new Neuron(new double[]{1}, 0, StandardFunctions.IDENTICAL.get());
        assertEquals(1, neuron.excite(new double[]{1}));
    }
}