package AILib.utills;

import AILib.agents.Agent;
import AILib.entities.Dataset;

import java.util.Arrays;

public class AgentChecker {
    private Agent agent;
    private boolean prints;

    public AgentChecker(Agent agent){
        this.agent = agent;
    }

    public AgentChecker(Agent agent, boolean isPrints){
        this.agent = agent;
        this.prints = isPrints;
    }

    public void isPrints(boolean prints){
        this.prints = prints;
    }

    public int check(double[][][] dataset, double roundRate){  // Returns count of passed tests
        int result = 0;
        for(double[][] data : dataset){
            double[] output = this.agent.start(data[0]);  // Contains AI's output. Will be casted to float[] answer soon
            float[] answer = new float[output.length];
            for(int i = 0; i < answer.length; i++)
                answer[i] = (float) ((float) Math.round(output[i] * Math.pow(10, roundRate)) / Math.pow(10, roundRate));

            //Casting dataset array to float
            float[] exampleOutput = new float[data[1].length];
            for(int i = 0; i < data[1].length; i++)
                exampleOutput[i] = (float) data[1][i];

            /*
                Casting values to float [] is needed to normalize the output and make the output readable
            */
            //Matching dataset array with AI's output
            if(Arrays.equals(answer, exampleOutput))
                result++;

            if(this.prints) {
                System.out.print(
                    "Output: " + Arrays.toString(answer) +
                    " Example: " + Arrays.toString(exampleOutput) + "\n"
                );
            }
        }

        if (this.prints)
            System.out.print(result + "/" + dataset.length);

        return result;
    }

    public int check(Dataset dataset, double roundRate){ return this.check(dataset.getDatasetArray(), roundRate); }
}
