package AILib.AIHandlerUtils;

import java.util.ArrayList;

public class ActionsMap{
    //Keys list, elements bind to [descriptions] and [actions] elements respectively
    private final ArrayList<String> keys;
    //Descriptions about action functional and how-to-use for user
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
