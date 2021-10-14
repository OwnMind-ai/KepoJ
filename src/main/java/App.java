import AILib.agents.SupervisedAgent;
import AILib.entities.Dataset;
import AILib.entities.AIFunctions;
import AILib.layers.StaticLayer;
import AILib.utills.AgentChecker;

public class App {
    public static void main(String[] args) {
        SupervisedAgent ai = new SupervisedAgent(9);
        ai.addAll(new StaticLayer(4, AIFunctions.SIGMOID),
            new StaticLayer(3, AIFunctions.SIGMOID),
            new StaticLayer(2, AIFunctions.SIGMOID));

        ai.learning(new Dataset("/home/ownmind/Documents/dat.bin").getDatasetArray(), 1);
        new AgentChecker(ai, true).check(new Dataset("/home/ownmind/Documents/dat.bin"), 1);
    }
}