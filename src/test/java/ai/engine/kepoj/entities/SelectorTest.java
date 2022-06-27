package ai.engine.kepoj.entities;

import ai.engine.kepoj.agents.deep.NeuralNetwork;
import ai.engine.kepoj.functions.StandardFunctions;
import ai.engine.kepoj.layers.Layer;
import ai.engine.kepoj.layers.StaticLayer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SelectorTest {

    @Test
    void choose() {
        Layer layer = new StaticLayer(4, StandardFunctions.IDENTICAL);
        NeuralNetwork agent = new NeuralNetwork(3);
        agent.addLayer(layer);

        for (int i = 0; i < 4; i++) {
            Collections.fill(layer.getNeuron(i).weights, i / 4d);
            layer.getNeuron(i).bias = 0;
        }

        Selector<String> selector = new Selector<>(agent, "0", "1", "2", "3");
        assertThrowsExactly(AssertionError.class, () -> new Selector<>(agent, "0", "1"));

        assertEquals("3", selector.choose(new double[]{1, 0, 0}));
        assertEquals("0", selector.choose(new double[]{1, -1.5, 0}));

        selector.setFilter(d -> d == 0.5);
        assertEquals("2", selector.choose(new double[]{1, 0, 0}));
        assertNull(selector.choose(new double[]{1, -1.5, 0}));
    }

    @Test
    void backwards() {
        Selector<String> selector = new Selector<>(new NeuralNetwork(5),
                "0", "1", "2", "3", "4"
        );

        assertArrayEquals(new double[]{0, 0, 1, 0, 0}, selector.backwards("2"));
        assertArrayEquals(new double[]{1, 0, 0, 0, 0}, selector.backwards("0"));
        assertArrayEquals(new double[]{0, 0, 0, 0, 1}, selector.backwards("4"));

        assertArrayEquals(new double[]{0, 0, 0, 0, 0}, selector.backwards("Forbidden"));
        assertArrayEquals(new double[]{0, 0, 0, 0, 0}, selector.backwards(null));
    }
}