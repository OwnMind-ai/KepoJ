package AILib.utills;

import AILib.agents.NeuralNetwork;
import AILib.functions.ActivationFunction;
import AILib.layers.Layer;

import java.util.ArrayList;
import java.util.HashMap;

public class NeuralNetworkReader {
    public static double VERSION = 1.2d;         // Version of saving format, not used in program
    public static double LAYER_SPLITTER = 2147483391;    // Keycode of start layer's structure description
    public static double WEIGHTS_START = 2147483390;     // Keycode of start neural network weights enumeration
    public static double FUNCTION_START = 2147483389;    // Keycode of ActivationFunction declaration start
    public static double FUNCTION_SPLITTER = 2147483388; // Keycode of splitter between functions

    private final FileReader reader;

    public NeuralNetworkReader(FileReader reader){
        this.reader = reader;
    }

    public NeuralNetwork read(){
        HashMap<Integer, ActivationFunction> functions = new HashMap<>();
        ArrayList<Layer> layers = new ArrayList<>();
        reader.next(); // Skip version

        while (reader.next() == FUNCTION_START){
            int hashCode = this.reader.next().intValue();
            String activation = this.parseFunction();
            this.reader.next();
            String derivative = this.parseFunction();

            functions.put(hashCode, new ActivationFunction(activation, derivative));
        }

        return null;
    }

    private String parseFunction(){
        StringBuilder function = new StringBuilder();

        while (reader.peek() != FUNCTION_SPLITTER &&
               reader.peek() != LAYER_SPLITTER &&
               reader.peek() != FUNCTION_START)
            function.append((char) reader.next().intValue());

        return function.toString();
    }
}
