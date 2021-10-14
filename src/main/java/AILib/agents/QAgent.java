package AILib.agents;

import java.util.Arrays;

public class QAgent extends NeuralNetwork {
    public QAgent(int inputNeurons) {
        super(inputNeurons);
    }

    public QAgent(String fileName) {
        super(fileName);
    }

    public void learningIteration(double reward, double[] nextState, double discountFactor, float ratio){
        double[] nextQValues = this.start(nextState);
        this.setError(reward, nextQValues, discountFactor);

        this.findError();
        this.backWeights(ratio);
    }

    private void setError(double reward, double[] nextQValues, double discountFactor){
        double[] errors = new double[this.layers.get(this.layers.size() - 1).getNeuronsLength()];
        Arrays.fill(errors, 0);
        Arrays.sort(nextQValues);

        double maxQ = Arrays.stream(nextQValues).summaryStatistics().getMax();
        int maxQIndex = Arrays.binarySearch(nextQValues, maxQ);
        errors[maxQIndex] =
                reward + maxQ * discountFactor - this.layers.get(this.layers.size() - 1).getOutputs()[maxQIndex];

        this.layers.get(this.layers.size() - 1).setErrors(errors);
    }

    private void findError(){               //Calculates error of all neurons(without output layer)
        for(int i = this.layers.size() - 2; i > 0; i--){
            this.layers.get(i).findErrors(
                    this.layers.get(i + 1).getErrors(),
                    this.layers.get(i + 1).getWeights()
            );
        }
    }

    private void backWeights(float ratio) {    //Changing weights of neurons. Ratio - learning coefficient
        for(int i = 1; i < this.layers.size(); i++)
            this.layers.get(i).trainLayer(this.layers.get(i - 1).getOutputs(), ratio);
    }
}
