package ai.engine.kepoj.layers;

import ai.engine.kepoj.functions.StandardFunctions;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class StaticLayerTest {
    private final StaticLayer layer;

    public StaticLayerTest() {
        this.layer = new StaticLayer(3, StandardFunctions.IDENTICAL);
        this.layer.buildLayer(new StaticLayer(3, StandardFunctions.IDENTICAL));

        for (int i = 0; i < this.layer.length(); i++) {
            Collections.fill(this.layer.getNeuron(i).weights, 0.5d);
            this.layer.getNeuron(i).bias = 0.25;
        }
    }

    @Test
    void doLayer() {
        StaticLayerTest test = new StaticLayerTest();

        test.layer.doLayer(new double[]{2, 1, 2});
        assertArrayEquals(new double[]{2.75, 2.75, 2.75}, test.layer.getOutputs());

        test.layer.doLayer(new double[]{-1, -1, 0});
        assertArrayEquals(new double[]{-0.75, -0.75, -0.75}, test.layer.getOutputs());

        test.layer.doLayer(new double[]{Double.MAX_VALUE, 0, 0});
        assertArrayEquals(new double[]{
                Double.MAX_VALUE / 2 + 0.25,
                Double.MAX_VALUE / 2 + 0.25,
                Double.MAX_VALUE / 2 + 0.25},
                test.layer.getOutputs()
        );
    }

    @Test
    void findErrors() {
        StaticLayerTest test = new StaticLayerTest();

        test.layer.doLayer(new double[]{2, 1, 2});
        test.layer.findErrors(new double[]{1, 1, 1}, new double[][]{
                {2, 2, 2}, {1,1,1}, {.5, .5, .5}
        });

        assertArrayEquals(new double[]{3.5, 3.5, 3.5}, test.layer.getErrors());
    }

    @Test
    void trainLayer() {
        StaticLayerTest test = new StaticLayerTest();

        test.layer.doLayer(new double[]{2, 1, 2});
        test.layer.findErrors(new double[]{3.5 - 0.75, -1.75, -0.75}, new double[][]{
                {1, 1, 1}, {1,1,1}, {.5, .5, .5}
        });

        test.layer.trainLayer(new double[]{0.75, 0.75, 0.75}, 1);
        test.layer.doLayer(new double[]{2, 1, 2});
        assertArrayEquals(new double[]{5.71875, 5.71875, 5.71875}, test.layer.getOutputs());
    }
}