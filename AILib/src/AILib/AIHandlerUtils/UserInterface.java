package AILib.AIHandlerUtils;

import java.util.*;

public abstract class UserInterface implements IUserInterface{
    private final String documentation;
    private final Scanner scanner;
    private final HashMap<String, String> outputBuffer;

    public UserInterface(HashMap<String, String> param, ActionsMap actionsList){
        this.scanner = new Scanner(System.in);
        this.documentation = getDocumentation(param, actionsList);
        this.outputBuffer = new HashMap<>();
    }

    public abstract String getDocumentation(HashMap<String, String> param, ActionsMap actionsList);
    public abstract void printCommandExceptionMessage();

    public String[] userInput(String inputMark){
        System.out.print(inputMark + " ");
        return this.scanner.nextLine()
                .split(" ");
    }

    public void printInterface(){
        System.out.println(this.documentation);
    }

    public void addToBuffer(String key, String value){
        this.outputBuffer.put(key, value);
    }
    public String getFromBuffer(String key){
        return this.outputBuffer.get(key);
    }
    public void removeFromBuffer(String index){
        this.outputBuffer.remove(index);
    }
    public void printFromBuffer(String index){
        System.out.println(this.outputBuffer.get(index));
    }

}
