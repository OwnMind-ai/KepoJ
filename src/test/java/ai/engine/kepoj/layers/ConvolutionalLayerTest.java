package ai.engine.kepoj.layers;

import ai.engine.kepoj.functions.StandardFunctions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ConvolutionalLayerTest {

    private void thresholdFillWeights(Layer layer, double weights, double bias){
        for (int i = 0; i < layer.length(); i++){
            for (int j = 0; j < layer.getNeuron(i).weights.size(); j++) {
                if(layer.getNeuron(i).weights.get(j) != 0)
                    layer.getNeuron(i).weights.set(j, weights);
            }

            layer.getNeuron(i).bias = bias;
        }
    }

    @Test
    void buildLayer() {
        ConvolutionalLayer layer = new ConvolutionalLayer(2, 2, StandardFunctions.IDENTICAL);
        layer.buildLayer(new InputLayer(9));
        thresholdFillWeights(layer, 1, 0);

        System.out.println(Arrays.deepToString(layer.getWeights()));
        assertArrayEquals(new double[][]{
                {1, 1, 0,
                 1, 1, 0,
                 0, 0, 0},
                {0, 1, 1,
                 0, 1, 1,
                 0, 0 ,0},
                {0, 0, 0,
                 1, 1, 0,
                 1, 1, 0},
                {0, 0, 0,
                 0, 1, 1,
                 0, 1, 1}
        },
        layer.getWeights());
    }
}