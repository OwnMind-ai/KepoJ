package ai.engine.kepoj.layers;

import ai.engine.kepoj.agents.deep.NeuralNetwork;
import ai.engine.kepoj.entities.Neuron;
import ai.engine.kepoj.functions.StandardFunctions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DynamicLayerTest {
    private void fillWeights(Layer layer, double weights, double bias){
        for (int i = 0; i < layer.length(); i++){
            Collections.fill(layer.getNeuron(i).weights, weights);
            layer.getNeuron(i).bias = bias;
        }
    }

    @Test
    void add() {
        StaticLayer next = new StaticLayer(3, StandardFunctions.IDENTICAL);
        DynamicLayer dynamic = new DynamicLayer(2, StandardFunctions.IDENTICAL, next);

        NeuralNetwork network = new NeuralNetwork(2);
        network.addAll(dynamic, next);

        fillWeights(next, 0.5, 0);
        fillWeights(dynamic, 0.3, 0);

        assertArrayEquals(new double[]{1.2d, 1.2d, 1.2d}, network.react(2, 2));

        dynamic.add(
                new Neuron(new double[]{0.2, 0.2}, 0, StandardFunctions.IDENTICAL.get()),
                new double[] {0.5, 0.5, 0.5}
        );

        assertArrayEquals(new double[]{1.6, 1.6, 1.6}, network.react(2, 2));
    }

    @Test
    void remove() {
        StaticLayer next = new StaticLayer(3, StandardFunctions.IDENTICAL);
        DynamicLayer dynamic = new DynamicLayer(4, StandardFunctions.IDENTICAL, next);

        NeuralNetwork network = new NeuralNetwork(2);
        network.addAll(dynamic, next);

        fillWeights(next, 0.5, 0);
        fillWeights(dynamic, 0.3, 0);

        assertArrayEquals(new double[]{2.4d, 2.4d, 2.4d}, network.react(2, 2));

        dynamic.remove(3);

        assertArrayEquals(new double[]{1.8d, 1.8d, 1.8d},
                Arrays.stream(network.react(2, 2)).map(d -> Math.round(d * 100000) / 100000d).toArray()
        );
    }
}