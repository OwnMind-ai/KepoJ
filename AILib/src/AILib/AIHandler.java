package AILib;

import AILib.AIHandlerUtils.ActionsMap;
import AILib.AIHandlerUtils.UserInterface;
import AILib.AILib.Dataset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class AIHandler extends Thread{
    private UserInterface userInterface;
    private final AI ai;
    private final ActionsMap actions;

    public AIHandler(AI ai){
        this.actions = this.buildActions();
        this.ai = ai;
        this.userInterface = new  UserInterface(this.getParameters(),this.actions) {
            public String getDocumentation(HashMap<String, String> param, ActionsMap actionsList) {
                StringBuilder output =
                    new StringBuilder(
                        "\nTime: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date()) +
                        "\nOpen: " + param.get("id") +
                        "\nStructure: " + param.get("structure") +
                        "\nActions:");
                for(int i = 0; i < actionsList.size(); i++)
                    output.append("\n").append("[").append(actionsList.getKey(i)).append("]").append(" - ").append(actionsList.getDescription(actionsList.getKey(i)));

                return output.toString();
            }

            public void printCommandExceptionMessage(){
                System.out.println("Invalid action");
            }
        };
    }

    public void setUserInterface(UserInterface userInterface){
        this.userInterface = userInterface;
    }

    @Override
    public void run(){
        this.userInterface.printInterface();
        for(String[] userInput = this.userInterface.userInput("->");
            !userInput[0].equals("exit");
            userInput = this.userInterface.userInput("->")
        ){
            try {
                this.actions.getActions(userInput[0]).run(Arrays.copyOfRange(userInput, 1, userInput.length, String[].class));
            }
            catch (Exception e){
                this.userInterface.printCommandExceptionMessage();
            }
            this.userInterface.printInterface();
        }
    }

    public HashMap<String, String> getParameters(){
        HashMap<String, String> param = new HashMap<>();
        param.put("id", this.ai.id);
        param.put("structure", this.ai.getAIParameters());

        return param;
    }

    public ActionsMap buildActions(){
        ActionsMap map = new ActionsMap();
        map.put("runAI","Starts current AI by include dataset", (args) -> this.runAI(args[0]));
        map.put("trainAI", "Trains current AI by include dataset",(args) -> this.trainAI(args[0], Float.parseFloat(args[1])));
        map.put("diagnostic", "Shows dataset's output values and AI's output, and compare them", (args) -> this.AIDiagnostic(args[0]));

        map.put("exit", "Shutdown program", null);
        return map;
    }

    private void runAI(String fileName){
        Dataset dataset = new Dataset(fileName);
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < dataset.getDatasetArray().length; i++) {
            text.append("\nInput: ")
                .append(Arrays.toString(dataset.getDatasetArray()[i][0]))
                .append("\nOutput: ")
                .append(Arrays.toString(this.ai.start(dataset.getDatasetArray()[i][0])));
        }

        this.userInterface.printText(text.toString());
    }

    private void trainAI(String fileName, float ratio){
        Thread aiThread = new Thread(() -> {
            this.ai.learning(new Dataset(fileName), ratio);
        });
        aiThread.start();

    }

    private void AIDiagnostic(String fileName){
        this.ai.AIChecker(new Dataset(fileName));
    }
}
