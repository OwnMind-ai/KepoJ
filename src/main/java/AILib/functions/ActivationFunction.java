package AILib.functions;

import java.io.Serializable;

public interface ActivationFunction extends Serializable {
    double activate(double input);
    double derivative(double input);

    static ActivationFunction create(IActivation activation, IActivation derivative){
        return new ActivationFunction() {
            @Override
            public double activate(double input) { return activation.run(input); }

            @Override
            public double derivative(double input) { return derivative.run(input); }
        };
    }
}

interface IActivation extends Serializable{
    double run(double input);
}