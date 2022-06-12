package ai.engine.kepoj.exceptions;

public class EntityParseException extends AgentException{
    private final String name, message;

    public EntityParseException(String message, Object name){
        this.message = message;
        this.name = name.getClass().getName();
    }

    @Override
    public String toString() {
        return message + " at class " + name;
    }
}
