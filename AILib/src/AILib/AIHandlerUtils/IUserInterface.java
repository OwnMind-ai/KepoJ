package AILib.AIHandlerUtils;

import java.util.HashMap;

public interface IUserInterface{
    String[] userInput(String inputMark);
    String getDocumentation(HashMap<String, String> parameters, ActionsMap actions);
    void printInterface(HashMap<String, String> parameters, ActionsMap actions);
    default void printText(String text){
        System.out.println(text);
    }
}
