package ai.engine.kepoj.exceptions;

/**
 * Throws when something wrong with agent initialization or running
 * @since 1.2
 */
public class AgentException extends RuntimeException{
    public AgentException(String message) {
        super(message);
    }

    public AgentException(){}
}