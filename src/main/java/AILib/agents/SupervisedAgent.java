package AILib.agents;

import AILib.entities.Dataset;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class SupervisedAgent extends NeuralNetwork implements Serializable {
    public SupervisedAgent(int inputNeurons) {
        super(inputNeurons);
    }

    public SupervisedAgent(String fileName) throws IOException, ClassNotFoundException {
        super(fileName);
    }

    private void findError(){
        for(int i = this.layers.size() - 2; i > 0; i--){
            this.layers.get(i).findErrors(
                    this.layers.get(i + 1).getErrors(),
                    this.layers.get(i + 1).getWeights()
            );
        }
    }

    private void backWeights(double ratio) {
        for(int i = 1; i < this.layers.size(); i++)
            this.layers.get(i).trainLayer(this.layers.get(i - 1).getOutputs(), ratio);
    }

    private void datasetOffset(double[] dataset){
        double[] errors = new double[this.layers.get(this.layers.size() - 1).size()];
        double[] outputs = this.layers.get(this.layers.size() - 1).getOutputs();

        for(int i = 0; i < outputs.length; i++){
            errors[i] = dataset[i] - outputs[i];
        }

        this.layers.get(this.layers.size() - 1).setErrors(errors);
    }

    public void train(double[][][] example, double ratio){
        double sumError = Double.MAX_VALUE;
        int age = 0;

        while (sumError >= this.fault) {
            sumError = 0;
            for (double[][] data : example) {
                double[] result = this.react(data[0]);
                for (int a = 0; a < result.length; a++)
                    sumError += Math.pow((data[1][a] - result[a]), 2);

                this.datasetOffset(data[1]);
                this.findError();
                this.backWeights(ratio);
            }

            System.out.println(age + " - " + sumError);
            age++;
        }
    }

    public void train(Dataset dataset, double ratio){ this.train(dataset.toArray(), ratio); }
}
