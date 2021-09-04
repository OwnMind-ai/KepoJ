import AILib.agents.AI;
import AILib.agents.SupervisedAgent;
import AILib.utills.AIFunctions;
import AILib.utills.Dataset;
import AILib.layers.StaticLayer;

public class App {
    public static void main(String[] args) {
        SupervisedAgent ai = new SupervisedAgent(9);
        ai.addAll(new StaticLayer(4, AIFunctions.SIGMOID),
            new StaticLayer(3, AIFunctions.SIGMOID),
            new StaticLayer(2, AIFunctions.SIGMOID));

        ai.learning(new Dataset("dat.bin").getDatasetArray(), 1);
        ai.AIChecker(new Dataset("dat.bin").getDatasetArray(), 1);
    }
}