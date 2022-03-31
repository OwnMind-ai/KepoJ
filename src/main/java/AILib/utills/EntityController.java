package AILib.utills;

import AILib.agents.Agent;
import AILib.anotations.Action;
import AILib.anotations.Parameter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class EntityController {
    public static final String INACTION = "inaction";

    private final ArrayList<ActionConfiguration> actions;
    private Method inactionAction;
    private final ArrayList<Field> parameters;

    private Object entity;
    private final Agent agent;

    private void load(){
        for (Method action : entity.getClass().getDeclaredMethods()) {
            if (action.isAnnotationPresent(Action.class)) {
                action.setAccessible(true);
                if (action.getAnnotation(Action.class).name().equals(INACTION)){
                    if(this.inactionAction == null) this.inactionAction = action;
                    else System.out.println("Inactive action has already taken");  // TODO: make exceptions
                }

                else if (action.getAnnotation(Action.class).id() < 0) {
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

    public double[] getParametersState(){
        return parameters.stream().mapToDouble(x -> {
            try {
                return x.getDouble(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return 0;
        }).toArray();
    }

    public String rawReact(double... data) throws Exception {
        double[] result = agent.react(data);
        System.out.println(Arrays.toString(result));
        // TODO: Fix this shit
        int maxId = ArrayUtils.getMaxIndex(result);
        Object[] actions = this.actions.stream()
                    .filter(item -> item.id == maxId).toArray();
        assert actions.length > 0;
        ActionConfiguration action = (ActionConfiguration) actions[0];

        if (action != null){
            if (action.threshold <= result[maxId]) {
                action.action.invoke(this.entity);
                return action.name;
            }
            else if(this.inactionAction != null) {
                this.inactionAction.invoke(this.entity);
                return INACTION;
            }
            else return null;
        } else { throw new Exception(""); }  //TODO: Make normal exceptions
    }

    public String react(double... data) throws Exception {
        double[] input = Arrays.copyOf(this.getParametersState(), parameters.size() + data.length);
        System.arraycopy(data, 0, input, parameters.size(), data.length);

        return this.rawReact(input);
    }

    public String react() throws Exception { return this.react(new double[0]); }

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