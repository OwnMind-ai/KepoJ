package AILib.utils;

import AILib.agents.Agent;
import AILib.entities.Dataset;

import java.util.Arrays;

public class AgentChecker {
    private final Agent agent;
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

    public int check(double[][][] dataset, double roundRate){
        int result = 0;
        for(double[][] data : dataset){
            double[] output = this.agent.react(data[0]);
            double[] answer = new double[output.length];
            for(int i = 0; i < answer.length; i++)
                answer[i] = Math.round(output[i] * Math.pow(10, roundRate)) / Math.pow(10, roundRate);

            double[] exampleOutput = new double[data[1].length];
            System.arraycopy(data[1], 0, exampleOutput, 0, data[1].length);

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
            System.out.println(result + "/" + dataset.length);

        return result;
    }

    public int check(Dataset dataset, double roundRate){ return this.check(dataset.toArray(), roundRate); }
}
