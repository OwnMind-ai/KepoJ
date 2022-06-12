package ai.engine.kepoj.agents;

import ai.engine.kepoj.entities.Dataset;
import ai.engine.kepoj.functions.StandardFunctions;
import ai.engine.kepoj.agents.deep.NeuralNetwork;
import ai.engine.kepoj.layers.StaticLayer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NeuralNetworkTest {
    private void diagonals(){
        Dataset dataset = new Dataset(new double[][][]{
                {{1, 1, -1, -1}, {0, 1}},
                {{-1, -1, 1, 1}, {1, 0}},
                {{0, 0, 0, 0}, {0, 0}}
        });

        NeuralNetwork network = new NeuralNetwork(4);
        network.addLayer(new StaticLayer(4, StandardFunctions.THRESHOLD));
        network.addLayer(new StaticLayer(2, StandardFunctions.THRESHOLD));
        network.setWeights(new double[]{-1,-1,0,0,-1, 1,1,0,0,-1, 0,0,-1,-1,-1, 0,0,1,1,-1,
                                         1,0,0,1,-1, 0,1,1,0,-1});

        for (double[][] data : dataset.toArray()) assertArrayEquals(data[1], network.react(data[0]));
    }

    @Test
    void react() {
        diagonals();
    }
}