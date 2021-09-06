import AILib.agents.QAgent;
import AILib.layers.StaticLayer;
import AILib.utills.AIFunctions;

import java.util.Arrays;

public class Test {
    public static int maxGameCount = 1000000;

    public static void main(String[] args){
        int AIScores = 0;
        int botScore = 0;
        double[] map = new double[]{0, 0, 0};
        int index = 0;

        QAgent ai = new QAgent(3);
        ai.addLayer(new StaticLayer(6, AIFunctions.BOUNDED_LEAKY_RELU));
        ai.addLayer(new StaticLayer(3, AIFunctions.BOUNDED_LEAKY_RELU));
        ai.addLayer(new StaticLayer(2, AIFunctions.SIGMOID));

        while (true){
            if(AIScores + botScore > Test.maxGameCount){
                AIScores = 0;
                botScore = 0;
            }

            double[] result = ai.start(map);
            double reward = 0;
            if((result[0] > result[1] && map[index] == 1) ||
                    (result[0] < result[1] && map[index] == 0)){
                AIScores++;
                reward = 1;
            }
            else{
                botScore++;
                reward = -1;
            }
            map[index] = map[index] == 0 ? 1 : 0;
            index++;
            if (index > 2) index = 0;

            ai.learningIteration(reward, map, 0.95, 0.5f);
            System.out.println("(" + AIScores + ", " + botScore + ") " +
                    "reward: " + reward + " map: " + Arrays.toString(map) + " result: " + Arrays.toString(result));
        }
    }
}