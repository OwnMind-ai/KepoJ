package ai.engine.kepoj.agents;

import ai.engine.kepoj.functions.StandardFunctions;
import ai.engine.kepoj.layers.Layer;
import ai.engine.kepoj.agents.deep.QNeuralNetwork;
import ai.engine.kepoj.layers.StaticLayer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class QNeuralNetworkTest {
    @Test
    public void setErrorTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        QNeuralNetwork agent = new QNeuralNetwork(3);
        Layer layer = new StaticLayer(3, StandardFunctions.IDENTICAL);
        agent.addLayer(layer);

        for (int i = 0; i < layer.length(); i++) {
            Collections.fill(layer.getNeuron(i).weights, 1d);
            layer.getNeuron(i).output = 0.5;
            layer.getNeuron(i).bias = 1;
        }

        Method setErrorMethod = agent.getClass().getDeclaredMethod("setError", double.class, double[].class, double.class);
        setErrorMethod.setAccessible(true);

        setErrorMethod.invoke(agent, 2, new double[]{2, 2, 2}, 0.5);
        assertArrayEquals(new double[]{2.5, 0.0, 0.0}, layer.getErrors());
    }
}