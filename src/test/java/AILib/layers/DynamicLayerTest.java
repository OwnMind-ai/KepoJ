package AILib.layers;

import AILib.agents.SupervisedAgent;
import AILib.functions.StandardFunctions;
import AILib.utils.AgentChecker;

import java.util.Scanner;


class DynamicLayerTest {
    public static void main(String[] args){
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
        StaticLayer next = new StaticLayer(2, StandardFunctions.SIGMOID);
        DynamicLayer dynamic = new DynamicLayer(3, StandardFunctions.SIGMOID, next);

        agent.addLayer(new StaticLayer(4, StandardFunctions.LEAKY_RELU));
        agent.addLayer(dynamic);
        agent.addLayer(next);

        // !! Long term action !!
        agent.train(example, 1);

        System.out.println(new AgentChecker(agent, true).check(example, 1));
        while (true){
            String n = new Scanner(System.in).next();
            if (n.equals("a")){
                dynamic.addNeuron();
            } else if (n.equals("t")) {
                agent.train(example, 1);
            } else {
                dynamic.removeNeuron(Integer.parseInt(n));
            }
            System.out.println(new AgentChecker(agent, true).check(example, 1));
        }
    }
}