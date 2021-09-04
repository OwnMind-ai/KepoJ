package AILib.agents;

import java.util.Arrays;

public class QAgent extends AI{
    public QAgent(int inputNeurons) {
        super(inputNeurons);
    }

    public QAgent(String fileName) {
        super(fileName);
    }

    private void setError(double reward, double[] nextQValues, double discountFactor){
        double[] errors = new double[this.layers.get(this.layers.size() - 1).getNeuronsLength()];
        Arrays.fill(errors, 0);
        Arrays.sort(nextQValues);

        double maxQ = Arrays.stream(nextQValues).summaryStatistics().getMax();
        int maxQIndex = Arrays.binarySearch(nextQValues, maxQ);
        errors[maxQIndex] =
                reward + maxQ * discountFactor - this.layers.get(this.layers.size() - 1).getOutputs()[maxQIndex];

        this.layers.get(this.layers.size() - 1).setError(errors);
    }
}
