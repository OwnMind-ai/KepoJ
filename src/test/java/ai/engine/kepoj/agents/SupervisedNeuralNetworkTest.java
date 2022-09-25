package ai.engine.kepoj.agents;

import ai.engine.kepoj.agents.deep.SupervisedNeuralNetwork;
import ai.engine.kepoj.entities.Dataset;
import ai.engine.kepoj.functions.StandardFunctions;
import ai.engine.kepoj.layers.ConvolutionalLayer;
import ai.engine.kepoj.layers.StaticLayer;
import ai.engine.kepoj.utils.AgentChecker;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        ConvolutionalLayer l = new ConvolutionalLayer(3, 1, 1,3, StandardFunctions.SIGMOID);

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

    @Test
    void t(){
        Dataset dataset = new Dataset(new double[0][][]);
        for (int i = 0; i < 256; i++) {
            int num = (int) (Math.random() * 64);
            String b = Integer.toBinaryString(num);
            double[] input = ((new String(new char[8 - b.length()]).replace("\0", "0")) + b)
                    .chars().mapToDouble(c -> Double.parseDouble(String.valueOf((char) c))).toArray();

            dataset.add(input, new double[]{num});
        }

        Dataset test = new Dataset(new double[0][][]);
        for (int i = 0; i < 64; i++) {
            int num = (int) (Math.random() * 64);
            String b = Integer.toBinaryString(num);
            double[] input = ((new String(new char[8 - b.length()]).replace("\0", "0")) + b)
                    .chars().mapToDouble(c -> Double.parseDouble(String.valueOf((char) c))).toArray();

            test.add(input, new double[]{num});
        }

        System.out.println(Arrays.deepToString(dataset.toArray()));

        SupervisedNeuralNetwork n =  new SupervisedNeuralNetwork(8);
        n.addAll(

                new StaticLayer(1, StandardFunctions.LEAKY_RELU)
        );

        n.isPrinting(true);
        n.train(dataset, 0.01, 0.05d);
        try {
            n.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new AgentChecker(n, true).check(test, 0);
    }
}