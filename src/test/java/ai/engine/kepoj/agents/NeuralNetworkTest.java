package ai.engine.kepoj.agents;

import ai.engine.kepoj.entities.Dataset;
import ai.engine.kepoj.entities.Neuron;
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
        setWeights(network, new double[]{-1,-1,0,0,-1, 1,1,0,0,-1, 0,0,-1,-1,-1, 0,0,1,1,-1,
                                         1,0,0,1,-1, 0,1,1,0,-1});

        for (double[][] data : dataset.toArray()) assertArrayEquals(data[1], network.react(data[0]));
    }

    public void setWeights(NeuralNetwork network, double[] weights) {
        int index = 0;
        for(int i = 1; i < network.getLayers().size(); i++)
            for(int a = 0; a < network.getLayers().get(i).length(); a++)
                for(int b = 0; b < network.getLayers().get(i).getNeuron(a).weights.size() + 1; b++)
                    setWeightForNeuron(network.getLayers().get(i).getNeuron(a), b, weights[index++]);
    }

    public void setWeightForNeuron(Neuron neuron, int index, double value){
        if(index == neuron.weights.size()) neuron.bias = value;
        else neuron.weights.set(index, value);
    }

    @Test
    void react() {
        diagonals();
    }
}