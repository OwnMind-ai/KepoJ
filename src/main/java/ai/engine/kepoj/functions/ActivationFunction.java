package ai.engine.kepoj.functions;

import java.io.Serializable;

/**
 * Complex of mathematical activation function and its derivative function
 * @see StandardFunctions
 * @since 1.2
 */
public interface ActivationFunction extends Serializable {
    ActivationFunction BOUNDED_LEAKY_RELU = ActivationFunction.create(
                        (x) -> (Math.min(1 + 0.01 * x, Math.max(0.01 * x, x))),
                        (x) -> ((x <= 0 || x >= 1) ? 0.01 : 1));

    ActivationFunction IDENTICAL = ActivationFunction.create((x) -> x, (x) -> 1);

    ActivationFunction LEAKY_RELU = ActivationFunction.create(
                        (x) -> Math.max(0.01 * x, x),
                        (x) -> (x >= 0 ? 1 : 0.01));

    ActivationFunction RELU = ActivationFunction.create(
                        (x) -> Math.max(0, x),
                        (x) -> (x >= 0 ? 1 : 0));

    ActivationFunction SIGMOID = ActivationFunction.create(
            (x) -> (1f / (1f + Math.pow(Math.E, -x))),
            (x) -> (1f / (1f + Math.pow(Math.E, -x))) * (1 - (1f / (1f + Math.pow(Math.E, -x)))));

    ActivationFunction TANH = ActivationFunction.create(
            (x) -> ((Math.pow(Math.E, x) - Math.pow(Math.E, -x)) / (Math.pow(Math.E, x) + Math.pow(Math.E, -x))),
            (x) -> (1 - Math.pow(((Math.pow(Math.E, x) - Math.pow(Math.E, -x)) / (Math.pow(Math.E, x) + Math.pow(Math.E, -x))), 2)));

    ActivationFunction THRESHOLD = ActivationFunction.create(
            (x) -> x >= 0 ? 1 : 0,
            (x) -> x != 0 ? 0 : 1);
    
    double activate(double input);
    double derivative(double input);

    /**
     * Creates an ActivationFunction object from lambdas of the activation and derivative functions
     * @param activation activation function
     * @param derivative derivative function
     * @since 1.2
     */
    static ActivationFunction create(Activatable activation, Activatable derivative){
        return new ActivationFunction() {
            @Override
            public double activate(double input) { return activation.run(input); }

            @Override
            public double derivative(double input) { return derivative.run(input); }
        };
    }
}

/**
 * A part of the activation function structure
 * @since 1.2
 */
@FunctionalInterface
interface Activatable extends Serializable{
    double run(double input);
}