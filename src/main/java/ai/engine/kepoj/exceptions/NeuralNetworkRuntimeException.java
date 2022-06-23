package ai.engine.kepoj.exceptions;

/**
 * Throws when something wrong with neural network initialization or running
 * @since 1.2
 */
public class NeuralNetworkRuntimeException extends AgentException{
    public NeuralNetworkRuntimeException(String message){
        super(message);
    }
}
