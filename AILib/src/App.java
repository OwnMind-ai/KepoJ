import AILib.AI;
import AILib.AILib.AIFunctions;
import AILib.AILib.Dataset;

public class App {
    public static void main(String[] args) {
        Dataset data =new Dataset("/home/ownmind/GitRepositories/AILib/dat.bin");
        AI ai = new AI(9, AIFunctions.SIGMOID);
        ai.addLayer(4);
        ai.addLayer(3);
        ai.addLayer(2);

        ai.learning(data, 1f);
        ai.AIChecker(data, 1);
    }
}