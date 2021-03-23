package AILib.AIHandlerUtils;

import java.util.ArrayList;
import java.util.HashMap;

public interface IUserInterface{
    String[] userInput(String inputMark);
    String getDocumentation(HashMap<String, String> parameters, ActionsMap actions);
    void printInterface();
    default void printText(String text){
        System.out.println(text);
    }
}
