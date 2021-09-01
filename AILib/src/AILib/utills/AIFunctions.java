package AILib.utills;

public enum AIFunctions{
    //Functions list
    SIGMOID((x) -> (1f/(1f + Math.pow(Math.E, -x))),
            (x) -> x * (1 - x)),

    TANH    ((x) -> ((Math.pow(Math.E, x) - Math.pow(Math.E, -x))/(Math.pow(Math.E, x) + Math.pow(Math.E, -x))),
             (x) -> (1 - Math.pow(x, 2))),

    RELU   ((x) -> Math.max(0,x),
            (x) -> (x >= 0 ? 1 : 0)),

    LEAKY_RELU((x) -> Math.max(0.01 * x,x),
               (x) -> (x >= 0 ? 1 : 0.01 * x)),

    BOUNDED_LEAKY_RELU ((x) -> (Math.min(1 + 0.01 * x, Math.max(0.01 * x, x))),
                        (x) -> ((x <= 0 || x >= 1) ? 0.01f : 1)),

    IDENTICAL ((x) -> x, (x) -> 1),

    SIGMOID_BOUND_2 ((x) -> (2 / (1 + Math.pow(Math.E, -x))),
                     (x) -> (x * (2 - x))),

    BOUNDED_LEAKY_RELU_BOUND_2 ((x) -> (Math.min(2 + 0.01 * x, Math.max(0.01 * x, x))),
                                (x) -> ((x <= 0 || x >= 2) ? 0.01f : 1)),;

    private final ActivationFunction activation;
    private final ActivationFunction derivative;
    AIFunctions(ActivationFunction activation, ActivationFunction derivative){
        this.activation = activation;
        this.derivative = derivative;
    }

    public double activationRun(double input){
        return this.activation.run(input);
    }

    public double derivativeRun(double input){
        return this.derivative.run(input);
    }
}

interface ActivationFunction{     //Lambda interface
    double run(double input);
}