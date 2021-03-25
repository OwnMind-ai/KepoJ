import AILib.AI;
import AILib.AIHandler;
import AILib.AILib.AIFunctions;

import java.util.Locale;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AI ai = null;

        try {
            System.out.print("Open exist or build new AI?(o/b)\n-> ");
            String input = scanner.nextLine();
            if (input.toLowerCase(Locale.ROOT).equals("o")) {
                System.out.print("\nType AI's filepath\n-> ");
                ai = new AI(scanner.nextLine());
            } else if (input.toLowerCase(Locale.ROOT).equals("b")) {
                System.out.print("Type first layer neurons count\n-> ");
                int firstNeurons = Integer.parseInt(scanner.nextLine());
                int AIFunctionsIndex = Integer.parseInt(scanner.nextLine());
                ai = new AI(firstNeurons, AIFunctions.values()[AIFunctionsIndex]);

                while (!input.equals("end")) {
                    try {
                        System.out.print(
                                "\nActions:\n" +
                                        "1. [addLayer NEURONS_COUNT]\n" +
                                        "2. [saveAI FILEPATH]\n" +
                                        "3. [end]\n" +
                                        "-> ");
                        input = scanner.nextLine();
                        if (input.split(" ")[0].equals("addLayer"))
                            ai.addLayer(Integer.parseInt(input.split(" ")[1]));
                        else if (input.split(" ")[0].equals("saveAI"))
                            ai.saveAI(input.split(" ")[1]);
                    }
                    catch (Exception e) { throw new RuntimeException("Invalid action");}
                }
            }
        }
        catch (Exception e){ throw new RuntimeException("Invalid action"); }

        AIHandler handler = new AIHandler(ai);
        handler.start();
    }
}