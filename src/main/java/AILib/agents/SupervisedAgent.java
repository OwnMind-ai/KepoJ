package AILib.agents;

import AILib.entities.Dataset;

import java.util.ArrayList;

public class SupervisedAgent extends NeuralNetwork {
    public SupervisedAgent(int inputNeurons) {
        super(inputNeurons);
    }

    public SupervisedAgent(String fileName) {
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

    private void backWeights(float ratio) {
        for(int i = 1; i < this.layers.size(); i++)
            this.layers.get(i).trainLayer(this.layers.get(i - 1).getOutputs(), ratio);
    }

    private void datasetOffset(double[] dataset){
        double[] errors = new double[this.layers.get(this.layers.size() - 1).getNeuronsLength()];
        double[] outputs = this.layers.get(this.layers.size() - 1).getOutputs();

        for(int i = 0; i < outputs.length; i++){
            errors[i] = dataset[i] - outputs[i];
        }

        this.layers.get(this.layers.size() - 1).setErrors(errors);
    }

    public double[] learning(double[][][] example, float ratio){
        ArrayList<Double> errorsLog = new ArrayList<>();
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
            errorsLog.add(sumError);
            System.out.println(age + " - " + sumError);
            age++;
        }

        return errorsLog.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public double[] learning(Dataset dataset, float ratio){ return this.learning(dataset.getDatasetArray(), ratio); }
}
