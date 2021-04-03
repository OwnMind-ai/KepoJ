package AILib.AIHandlerUtils;

import java.util.*;

public abstract class UserInterface implements IUserInterface{
    protected final Scanner scanner;                             //User input scanner
    protected final HashMap<BufferKeys, String> outputBuffer;    //Text buffer for keeping temporal text

    public UserInterface(){
        this.scanner = new Scanner(System.in);
        this.outputBuffer = new HashMap<>();

        this.addToBuffer(BufferKeys.LEARNING_STATUS, "");   //Setting to null learning status message
    }

    public abstract String getDocumentation(HashMap<String, String> param, ActionsMap actionsList);
    public abstract void printCommandExceptionMessage();

    //Returns user's commands in array representation
    public String[] userInput(String inputMark){
        System.out.print(inputMark + " ");
        return this.scanner.nextLine()
                .split(" ");
    }

    public void printInterface(HashMap<String, String> param, ActionsMap actionsList){
        System.out.println(this.getDocumentation(param, actionsList));
    }

    /*Buffer Commands*/
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
