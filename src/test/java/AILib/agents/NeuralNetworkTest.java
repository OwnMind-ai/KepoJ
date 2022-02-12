package AILib.agents;

import AILib.entities.AIFunctions;
import AILib.entities.Dataset;
import AILib.layers.StaticLayer;
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
        network.addLayer(new StaticLayer(4, AIFunctions.THRESHOLD));
        network.addLayer(new StaticLayer(2, AIFunctions.THRESHOLD));
        network.setWeights(new double[]{-1,-1,0,0,-1, 1,1,0,0,-1, 0,0,-1,-1,-1, 0,0,1,1,-1,
                                         1,0,0,1,-1, 0,1,1,0,-1});

        for (double[][] data : dataset.toArray()) assertArrayEquals(data[1], network.react(data[0]));
    }

    @Test
    void react() {
        diagonals();
    }
}