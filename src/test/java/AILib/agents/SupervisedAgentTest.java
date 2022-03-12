package AILib.agents;

import AILib.functions.ActivationFunction;
import AILib.functions.StandardFunctions;
import AILib.layers.StaticLayer;
import AILib.utills.AgentChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupervisedAgentTest {

    @Test
    void lines() throws Exception {
        double[][][] example = {
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
                {{0,0,0,0,0,0,0,0,0},{0.2d,0.2d}}};

        ActivationFunction activationFunction = ActivationFunction.generate(
                "1 / (1 + e ** (-x))",
                "x * (1 - x)"
        );

        SupervisedAgent agent = new SupervisedAgent(9);
        agent.addLayer(new StaticLayer(4, activationFunction));
        agent.addLayer(new StaticLayer(3, activationFunction));
        agent.addLayer(new StaticLayer(2, activationFunction));

        // !! Long term action !!
        agent.train(example, 1);

        int testResult = new AgentChecker(agent).check(example, 1);
        assertEquals(example.length, testResult);
    }
}