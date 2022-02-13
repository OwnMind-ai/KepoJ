package AILib.agents;

import AILib.functions.StandardFunctions;
import AILib.layers.StaticLayer;
import AILib.utills.AgentChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupervisedAgentTest {

    @Test
    void lines() {
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

        SupervisedAgent agent = new SupervisedAgent(9);
        agent.addLayer(new StaticLayer(4, StandardFunctions.SIGMOID));
        agent.addLayer(new StaticLayer(3, StandardFunctions.SIGMOID));
        agent.addLayer(new StaticLayer(2, StandardFunctions.SIGMOID));

        // !! Long term action !!
        agent.train(example, 1);

        int testResult = new AgentChecker(agent).check(example, 1);
        assertEquals(example.length, testResult);
    }
}