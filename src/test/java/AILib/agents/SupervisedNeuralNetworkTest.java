package AILib.agents;

import AILib.agents.deep.SupervisedNeuralNetwork;
import AILib.entities.Dataset;
import AILib.functions.StandardFunctions;
import AILib.layers.ConvolutionalLayer;
import AILib.layers.StaticLayer;
import AILib.utils.AgentChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupervisedNeuralNetworkTest {

    @Test
    void lines() {
        Dataset example = new Dataset(new double[][][]{
                {{1,1,1,0,0,0,0,0,0},{0.8d,0.2d}},
                {{0,0,0,1,1,1,0,0,0},{0.8d,0.2d}},
                {{0,0,0,0,0,0,1,1,1},{0.8d,0.2d}},
                {{1,0,0,1,0,0,1,0,0},{0.2d,0.8d}},
                {{0,1,0,0,1,0,0,1,0},{0.2d,0.8d}},
                {{0,0,1,0,0,1,0,0,1},{0.2d,0.8d}},
                {{1,1,1,1,0,0,1,0,0},{0.8d,0.8d}},
                {{1,1,1,0,1,0,0,1,0},{0.8d,0.8d}},
                {{1,1,1,0,0,1,0,0,1},{0.8d,0.8d}},
                {{1,0,0,1,1,1,1,0,0},{0.8d,0.8d}},
                {{0,1,0,1,1,1,0,1,0},{0.8d,0.8d}},
                {{0,0,1,1,1,1,0,0,1},{0.8d,0.8d}},
                {{1,0,0,1,0,0,1,1,1},{0.8d,0.8d}},
                {{0,1,0,0,1,0,1,1,1},{0.8d,0.8d}},
                {{0,0,1,0,0,1,1,1,1},{0.8d,0.8d}},
                {{0,0,0,0,0,0,0,0,0},{0.2d,0.2d}}});

        ConvolutionalLayer l = new ConvolutionalLayer(3, 1, StandardFunctions.SIGMOID);
        l.setSize(1, 3);

        SupervisedNeuralNetwork agent = new SupervisedNeuralNetwork(9);
        agent.addLayer(l);
        agent.addLayer(new StaticLayer(3, StandardFunctions.SIGMOID));
        agent.addLayer(new StaticLayer(2, StandardFunctions.SIGMOID));
        agent.isPrinting(true);

        // !! Long term action !!
        agent.train(example, 1, 0.0005d);

        int testResult = new AgentChecker(agent).check(example, 1);
        assertEquals(example.size(), testResult);
    }
}