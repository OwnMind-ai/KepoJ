import AILib.agents.AI;
import AILib.agents.SupervisedAgent;
import AILib.utills.Dataset;

public class Test {
    public static void main(String[] args){
        SupervisedAgent ai = new SupervisedAgent("test1.ai");
        ai.AIChecker(new Dataset("dat.bin").getDatasetArray(), 1);
    }
}