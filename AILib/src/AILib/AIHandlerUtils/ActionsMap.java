package AILib.AIHandlerUtils;

import java.util.ArrayList;

public class ActionsMap{
    private final ArrayList<String> keys;
    private final ArrayList<String> descriptions;
    private final ArrayList<HandlerAction> actions;

    public ActionsMap(){
        this.keys = new ArrayList<>();
        this.descriptions = new ArrayList<>();
        this.actions = new ArrayList<>();
    }

    public String getKey(int index){
        return this.keys.get(index);
    }
    public String getDescription(String key) {
        return this.descriptions.get(this.keys.indexOf(key));
    }
    public HandlerAction getActions(String key) {
        return this.actions.get(this.keys.indexOf(key));
    }
    public void put(String key, String description, HandlerAction action){
        this.keys.add(key);
        this.descriptions.add(this.keys.indexOf(key), description);
        this.actions.add(this.keys.indexOf(key), action);
    }
    public int size(){
        return this.keys.size();
    }
}
