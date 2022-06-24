package ai.engine.kepoj.functions;

import java.io.Serializable;

/**
 * Complex of mathematical activation function and its derivative function
 * @see StandardFunctions
 * @since 1.2
 */
public interface ActivationFunction extends Serializable {
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