package AILib.utills;

import AILib.agents.Agent;
import AILib.anotations.Action;
import AILib.anotations.Parameter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class EntityController {
    private final ArrayList<ActionConfiguration> actions;
    private final ArrayList<Field> parameters;

    private Object entity;
    private final Agent agent;

    private void load(){
        for (Method action : entity.getClass().getDeclaredMethods()) {
            if (action.isAnnotationPresent(Action.class)) {
                action.setAccessible(true);
                if (action.getAnnotation(Action.class).id() < 0) {
                    actions.add(new ActionConfiguration(
                            action.getAnnotation(Action.class),
                            this.actions.size(),
                            action
                    ));
                } else { actions.add(new ActionConfiguration(action.getAnnotation(Action.class), action)); }
            }
        }

        for (Field parameter : entity.getClass().getDeclaredFields()) {
            if (parameter.isAnnotationPresent(Parameter.class)){
                parameter.setAccessible(true);
                Parameter configuration = parameter.getAnnotation(Parameter.class);

                if (configuration.id() >= 0) this.parameters.add(configuration.id(), parameter);
                else this.parameters.add(parameter);
            }
        }
    }

    public void bind(Object entity){
        this.entity = entity;
        this.load();
    }

    public EntityController(Agent agent, Object entity) {
        this.agent = agent;
        this.entity = entity;

        this.actions = new ArrayList<>();
        this.parameters = new ArrayList<>();

        this.load();
    }

    public EntityController(Agent agent){
        this.agent = agent;

        this.actions = new ArrayList<>();
        this.parameters = new ArrayList<>();
    }

    public void react() throws Exception {
        double[] result = agent.react(parameters.stream().mapToDouble(x -> {
            try {
                return x.getDouble(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return 0;
        }).toArray());

        int max = Arrays.binarySearch(result, Arrays.stream(result).summaryStatistics().getMax());
        ActionConfiguration action = (ActionConfiguration) this.actions.stream()
                    .filter(item -> item.id == max).toArray()[0];

        if (action != null){
            System.out.println(Arrays.toString(result));
            System.out.println("Invoke: " + action.name);
            if (action.threshold <= max) action.action.invoke(this.entity);
        } else { throw new Exception(""); }  //TODO: Make normal exceptions
    }
}

class ActionConfiguration{
    public final int id;
    public final double threshold;
    public final String name;

    public final Method action;

    public ActionConfiguration(Action annotation, Method action){
        this.id = annotation.id();
        this.threshold = annotation.threshold();
        this.name = annotation.name();
        this.action = action;
    }

    public ActionConfiguration(Action annotation, int id, Method action){
        this.id = id;
        this.threshold = annotation.threshold();
        this.name = annotation.name();
        this.action = action;
    }

    @Override
    public String toString() {
        return "ActionConfiguration{" +
                "id=" + id +
                ", threshold=" + threshold +
                ", name='" + name + '\'' +
                ", action=" + action +
                '}';
    }
}