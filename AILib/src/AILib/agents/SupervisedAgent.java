package AILib.agents;

import AILib.utills.Dataset;

import java.util.ArrayList;

public class SupervisedAgent extends AI{
    public SupervisedAgent(int inputNeurons) {
        super(inputNeurons);
    }

    public SupervisedAgent(String fileName) {
        super(fileName);
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

    private void datasetOffset(double[] dataset){
        double[] errors = new double[this.layers.get(this.layers.size() - 1).getNeuronsLength()];
        double[] outputs = this.layers.get(this.layers.size() - 1).getOutputs();

        for(int i = 0; i < outputs.length; i++){
            errors[i] = dataset[i] - outputs[i];
        }

        this.layers.get(this.layers.size() - 1).setError(errors);
    }

    public double[] learning(double[][][] example, float ratio){  //Trains AI by following dataset array and learning ratio
        ArrayList<Double> errorsLog = new ArrayList<>();
        double sumError = Double.MAX_VALUE;     //(setting to maximum value for first while iteration)
        int age = 0;

        while (sumError >= this.fault) {    //Training continues until the error is less than the minimum([this.fault])
            sumError = 0;
            for (double[][] data : example) {
                //Calculating current error by example
                double[] result = this.start(data[0]);
                for (int a = 0; a < result.length; a++)
                    sumError += Math.pow((data[1][a] - result[a]), 2);

                this.datasetOffset(data[1]);  //Calculates error of output layer
                this.findError();              //Calculates error of all neurons(without output layer)
                this.backWeights(ratio);       //Changing weights of neurons
            }
            errorsLog.add(sumError);
            System.out.println(age + " - " + sumError);
            age++;
        }

        return errorsLog.stream().mapToDouble(Double::doubleValue).toArray();
    }

    //Learning by following Dataset class
    public double[] learning(Dataset dataset, float ratio){ return this.learning(dataset.getDatasetArray(), ratio); }
}
