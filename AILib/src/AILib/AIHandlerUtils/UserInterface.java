package AILib.AIHandlerUtils;

import java.util.*;

public abstract class UserInterface implements IUserInterface{
    protected final Scanner scanner;
    protected final HashMap<BufferKeys, String> outputBuffer;

    public UserInterface(){
        this.scanner = new Scanner(System.in);
        this.outputBuffer = new HashMap<>();

        this.addToBuffer(BufferKeys.LEARNING_STATUS, "");
    }

    public abstract String getDocumentation(HashMap<String, String> param, ActionsMap actionsList);
    public abstract void printCommandExceptionMessage();

    public String[] userInput(String inputMark){
        System.out.print(inputMark + " ");
        return this.scanner.nextLine()
                .split(" ");
    }

    public void printInterface(HashMap<String, String> param, ActionsMap actionsList){
        System.out.println(this.getDocumentation(param, actionsList));
    }

    public void addToBuffer(BufferKeys key, String value){
        this.outputBuffer.put(key, value);
    }
    public String getFromBuffer(BufferKeys key){
        return this.outputBuffer.get(key);
    }
    public void removeFromBuffer(BufferKeys key){
        this.outputBuffer.remove(key);
    }
    public void printFromBuffer(BufferKeys key){
        System.out.println(this.outputBuffer.get(key));
    }
    public String setBufferValue(BufferKeys key, String value){
        return this.outputBuffer.replace(key, value);
    }
    public String useFromBuffer(BufferKeys key){
        return this.outputBuffer.replace(key, "");
    }
}
