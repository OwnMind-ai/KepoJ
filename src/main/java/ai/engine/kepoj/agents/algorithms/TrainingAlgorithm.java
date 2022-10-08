package ai.engine.kepoj.agents.algorithms;

import ai.engine.kepoj.agents.Agent;

/** Points to the agent that can be trained. All these classes can wrap a base agent class <A>.
 * @param <T> Wrapper implementation class
 * @param <A> Agent class that can be wrapped
 * @see SupervisedAlgorithm
 * @see QAlgorithm
 * @since 1.3
 */
public interface TrainingAlgorithm<T extends TrainingAlgorithm<T, A>, A extends Agent> extends Agent{
    /** Wraps an Agent instance with training algorithm.
     * Wrapped instances still connected to the agent instance.
     * If you want break that connection, use Agent::clone() instead.
     * @param agent a base agent instance
     * @return wrapped instance by training algorithm implementation
     * @since 1.3
     */
    T wrap(A agent);
}
