package AILib.utils;

import AILib.agents.Agent;
import AILib.anotations.Action;
import AILib.anotations.ActionsList;
import AILib.anotations.Parameter;
import AILib.exceptions.EntityParseException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class EntityController {
    public static final String INACTION = "inaction";

    private ArrayList<ActionConfiguration> actions;
    private Method inactionAction;
    private final ArrayList<Field> parameters;

    private Object entity;
    private final Agent agent;

    private void loadAction(Method action){
        action.setAccessible(true);
        if (action.getAnnotation(Action.class).name().equals(INACTION)){
            assert this.inactionAction != null :
                    new EntityParseException("Inactive action has already taken", this.entity);
            this.inactionAction = action;
        }

        else if (action.getAnnotation(Action.class).id() < 0) {
            actions.add(new ActionConfiguration(
                    action.getAnnotation(Action.class),
                    this.actions.size(),
                    action
            ));
        } else { actions.add(new ActionConfiguration(action.getAnnotation(Action.class), action)); }
    }

    private void loadParameter(Field parameter){
        parameter.setAccessible(true);
        Parameter configuration = parameter.getAnnotation(Parameter.class);

        if (configuration.id() >= 0) this.parameters.add(configuration.id(), parameter);
        else this.parameters.add(parameter);
    }

    private void loadActionsList(Method method){
        method.setAccessible(true);
        assert method.getParameterCount() == 1 :
                new EntityParseException(
                        "ActionsList method \"" + method.getName() + "\" must have one String parameter", this.entity);

        ActionsList annotation = method.getAnnotation(ActionsList.class);

        for (int i = 0; i < annotation.names().length; i++) {
            ActionConfiguration configuration = new ActionConfiguration(
                    this.actions.size(),
                    0,
                    annotation.names()[i],
                    method
            );
            configuration.isListObject(true);

            this.actions.add(configuration);
        }
    }

    private void load() {
        for (Method action : entity.getClass().getDeclaredMethods()) {
            if (action.isAnnotationPresent(Action.class))
                this.loadAction(action);
            else if (action.isAnnotationPresent(ActionsList.class))
                this.loadActionsList(action);
        }

        assert this.agent.outputLength() == this.actions.size() :
                new EntityParseException("Entity has less/more actions than agent provide", this.entity);

        this.actions = this.actions.stream()
                .sorted(Comparator.comparingInt(ActionConfiguration::getId))
                .collect(Collectors.toCollection(ArrayList::new));


        for (Field parameter : entity.getClass().getDeclaredFields())
            if (parameter.isAnnotationPresent(Parameter.class))
               this.loadParameter(parameter);
    }

    public void bind(Object entity) throws EntityParseException {
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

        int maxId = ArrayUtils.getMaxIndex(result);
        ActionConfiguration action = this.actions.get(maxId);

        if (action.threshold <= result[maxId]) {
            if(action.isListObject()) action.method.invoke(this.entity, action.name);
            else action.method.invoke(this.entity);

            return action.name;
        }
        else if(this.inactionAction != null) {
            this.inactionAction.invoke(this.entity);
            return INACTION;
        }
        else return null;
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
    public final Method method;

    private boolean listObject = false;

    public ActionConfiguration(Action annotation, Method action){
        this.id = annotation.id();
        this.threshold = annotation.threshold();
        this.name = annotation.name();
        this.method = action;
    }

    public ActionConfiguration(Action annotation, int id, Method action){
        this.id = id;
        this.threshold = annotation.threshold();
        this.name = annotation.name();
        this.method = action;
    }

    public ActionConfiguration(int id, double threshold, String name, Method method) {
        this.id = id;
        this.threshold = threshold;
        this.name = name;
        this.method = method;
    }

    public void isListObject(boolean value) {
        this.listObject = value;
    }

    public boolean isListObject(){
        return this.listObject;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ActionConfiguration{" +
                "id=" + id +
                ", threshold=" + threshold +
                ", name='" + name + '\'' +
                ", action=" + method +
                '}';
    }
}