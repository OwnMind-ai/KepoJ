package AILib.AILib;

public enum AIFunctions{
    SIGMOID((x) -> (1f/(1f + Math.pow(Math.E, -x))),
            (x) -> x * (1 - x)),
    RELU   ((x) -> Math.max(0,x),
            (x) -> { if(x >= 0){return 1;} else{return 0;}}),
    MYRELU ((x) -> (Math.min(1 + 0.01 * x, Math.max(0.01 * x, x))),
            (x) -> {if(x <= 0 || x >= 1) return 0.01f; else return 1;});

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

interface ActivationFunction{
    double run(double input);
}