package ai.engine.kepoj.entities;

import ai.engine.kepoj.agents.Agent;
import ai.engine.kepoj.utils.ArrayUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoublePredicate;

/**
 * The selector class represents agent output as a single choice, not as a doubles array.
 * To get choice, the agent's output filters by Selector.filter, that can be set by Selector::setFilter method,
 * and takes the index of maximum value from the filtered array. Each choice is correlated with the agent's output index
 * @param <T> choice type
 * @since 1.3
 */
public class Selector<T> {
    private final Agent agent;
    private final T[] choices;
    private DoublePredicate filter = d -> true;

    @SafeVarargs
    public Selector(Agent agent, T... choices) {
        assert choices.length == agent.outputLength() : "Number of choices and agent output length don't match";

        this.agent = agent;
        this.choices = choices;
    }

    /**
     * @return choices array
     * @since 1.3
     */
    public T[] getChoices() {
        return choices;
    }

    /**
     * Sets a filter for output values
     * @param filter filter function
     * @since 1.3
     */
    public void setFilter(DoublePredicate filter) {
        this.filter = filter;
    }

    /**
     * Returns a choice object according to the agent's output
     * @param input input data for agent
     * @return choice instance
     * @since 1.3
     */
    public T choose(double[] input){
        double[] result = this.agent.react(input);

        int max = ArrayUtils.getMaxIndex(Arrays.stream(result)
                .map(d -> this.filter.test(d) ? d : Double.NEGATIVE_INFINITY)
                .toArray());

        return max < 0 ? null : this.choices[max];
    }

    public double[] raw(double[] input){
        return this.agent.react(input);
    }

    /**
     * Creates doubles array, where value at the index of provided choice is 1
     * @param choice choice object
     * @return represented choice object as a doubles array
     * @since 1.3
     */
    public double[] backwards(T choice){
        return Arrays.stream(this.choices).mapToDouble(t -> Objects.equals(t, choice) ? 1 : 0).toArray();
    }
}
