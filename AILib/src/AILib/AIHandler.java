package AILib;

import AILib.AIHandlerUtils.ActionsMap;
import AILib.AIHandlerUtils.BufferKeys;
import AILib.AIHandlerUtils.UserInterface;
import AILib.AILib.Dataset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class AIHandler extends Thread{
    private UserInterface userInterface;
    private final AI ai;
    private final ActionsMap actions;
    private int autoSaveDelay = 0;
    private String autoSaveFilepath = null;

    public AIHandler(AI ai){
        this.actions = this.buildActions();
        this.ai = ai;
        this.userInterface = new UserInterface() {
            public String getDocumentation(HashMap<String, String> param, ActionsMap actionsList) {
                StringBuilder output = new StringBuilder();
                output.append(this.useFromBuffer(BufferKeys.LEARNING_STATUS))
                        .append("\nTime: ").append((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date()))
                        .append("\nOpen: ").append(param.get("id"))
                        .append("\nStructure: ").append(param.get("structure"))
                        .append("\nActions:");
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
        this.userInterface.printInterface(this.getParameters(),this.actions);
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
            this.userInterface.printInterface(this.getParameters(),this.actions);
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
        map.put("runAI","Starts current AI by indicated dataset. [runAI FILEPATH]", (args) -> this.runAI(args[0]));
        map.put("trainAI", "Trains current AI by indicated dataset. [trainAI FILEPATH RATIO]",(args) -> this.trainAI(args[0], Float.parseFloat(args[1])));
        map.put("diagnostic", "Shows dataset's output values and AI's output, and compare them. [diagnostic FILEPATH ROUND_RATIO]", (args) -> this.AIDiagnostic(args[0], Integer.parseInt(args[1])));
        map.put("saveAI", "Saves AI to indicated file. [saveAI FILEPATH]", (args) -> this.saveAI(args[0]));
        map.put("setFault","Sets AI's fault value. [setFault VALUE]",(args) -> this.ai.setFault(Float.parseFloat(args[0])));
        map.put("toggleAutoSave", "Turns on auto save to indicated file. [toggleAutoSave FILEPATH TIME_DELAY]", (args) -> {
            this.autoSaveDelay = Integer.parseInt(args[1]);
            this.autoSaveFilepath = args[0];
        });

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
            this.userInterface.addToBuffer(BufferKeys.LEARNING_STATUS, "\nLearning complete. Use [diagnose] to check\n");
        });
        if(this.autoSaveDelay > 0 && this.autoSaveFilepath != null){
            Thread autoSaveThread = new Thread(() -> {
                while (true){
                    this.ai.saveAI(this.autoSaveFilepath);
                    try {
                        Thread.sleep(this.autoSaveDelay * 1000L);
                    } catch (InterruptedException ignored){}
                }
            });
            autoSaveThread.start();
        }
        aiThread.start();
    }

    private void AIDiagnostic(String fileName, int roundRate){
        this.ai.AIChecker(new Dataset(fileName), roundRate);
    }

    private void saveAI(String fileName){
        try{
            this.ai.saveAI(fileName);
            this.userInterface.printText("Save done! Directory: " + fileName);
        }
        catch(Exception e){
            this.userInterface.printText("Error, saving cannot be performed in the specified directory.");
        }
    }
}
