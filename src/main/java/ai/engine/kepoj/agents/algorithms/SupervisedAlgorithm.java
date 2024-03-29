package ai.engine.kepoj.agents.algorithms;

import ai.engine.kepoj.entities.Dataset;

/**
 * Points to a class that implements learning by provided dataset.
 * Training realized in two forms - by the minimal error and by ages count
 * @since 1.2
 * @see ai.engine.kepoj.agents.deep.SupervisedNeuralNetwork
 */
public interface SupervisedAlgorithm {
    double train(Dataset dataset, double ratio, double fault);

    double train(Dataset dataset, double ratio, long ages);
}
