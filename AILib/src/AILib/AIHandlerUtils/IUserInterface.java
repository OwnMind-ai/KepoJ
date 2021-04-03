package AILib.AIHandlerUtils;

import java.util.HashMap;

public interface IUserInterface{
    //Returns user's commands in array representation
    String[] userInput(String inputMark);
    //Returns documentation, that realized after extending UserInterface class
    String getDocumentation(HashMap<String, String> parameters, ActionsMap actions);
    //Prints UI to user
    void printInterface(HashMap<String, String> parameters, ActionsMap actions);

    default void printText(String text){
        System.out.println(text);
    }
}
