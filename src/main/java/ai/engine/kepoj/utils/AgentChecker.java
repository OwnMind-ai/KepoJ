package ai.engine.kepoj.utils;

import ai.engine.kepoj.entities.Dataset;
import ai.engine.kepoj.agents.Agent;

import java.util.Arrays;

/**
 * Tests the agent for the correctness of answers according to the dataset
 * @since 1.0
 */
public class AgentChecker {
    private final Agent agent;
    private boolean prints;

    /**
     * @param agent agent to test
     * @since 1.1
     */
    public AgentChecker(Agent agent){
        this.agent = agent;
    }

    /**
     * @param agent agent to test
     * @param isPrints specifies that the checker will print checking result
     * @since 1.1
     */
    public AgentChecker(Agent agent, boolean isPrints){
        this.agent = agent;
        this.prints = isPrints;
    }

    /**
     * Specifies that the checker will print checking result
     * @since 1.1
     */
    public void isPrints(boolean prints){
        this.prints = prints;
    }

    /**
     * Test the agent according to the dataset
     * @param dataset dataset with input values and correct answers
     * @param roundRate specifies rounding level of results
     * @return number of passed test
     */
    public int check(Dataset dataset, double roundRate){
        int result = 0;
        for(double[][] data : dataset.toArray()){
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
            System.out.println(result + "/" + dataset.size());

        return result;
    }
}
