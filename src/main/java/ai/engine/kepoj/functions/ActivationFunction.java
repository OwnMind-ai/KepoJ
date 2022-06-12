package ai.engine.kepoj.functions;

import java.io.Serializable;

public interface ActivationFunction extends Serializable {
    double activate(double input);
    double derivative(double input);

    static ActivationFunction create(Activatable activation, Activatable derivative){
        return new ActivationFunction() {
            @Override
            public double activate(double input) { return activation.run(input); }

            @Override
            public double derivative(double input) { return derivative.run(input); }
        };
    }
}

interface Activatable extends Serializable{
    double run(double input);
}