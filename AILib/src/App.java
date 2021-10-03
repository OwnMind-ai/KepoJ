import AILib.agents.AI;
import AILib.agents.SupervisedAgent;
import AILib.layers.ConvolutionalLayer;
import AILib.utills.AIFunctions;
import AILib.utills.Dataset;
import AILib.layers.StaticLayer;

public class App {
    public static void main(String[] args) {
        SupervisedAgent ai = new SupervisedAgent(9);
        ai.addAll(new ConvolutionalLayer(1, 1, 3, 3, AIFunctions.SIGMOID),
            new StaticLayer(3, AIFunctions.SIGMOID),
            new StaticLayer(2, AIFunctions.SIGMOID));

        ai.save("test1.ai");
        //ai.learning(new Dataset("dat.bin").getDatasetArray(), 1);
        //ai.AIChecker(new Dataset("dat.bin").getDatasetArray(), 1);

    }
}