package AILib.functions;

public enum StandardFunctions {
    SIGMOID("1 / (1 + pow(e, -x))",
            "x * (1 - x)"),

    TANH    ("(pow(e, x) - pow(e, -x)) / (pow(e, x) + pow(e, -x))",
            "1 - pow(x, 2))"),

    RELU   ("max(0, x)",
            "x >= 0"),

    LEAKY_RELU("max(0.01 * x, x)",
            "if(x >= 0, 1, 0.01 * x)"),

    BOUNDED_LEAKY_RELU ("min(1 + 0.01 * x, max(0.01 * x, x))",
            "if(x <= 0 || x >= 1, 0.01, 1)"),

    IDENTICAL ("x", "1"),

    THRESHOLD("x >= 0",
            "x == 0");

    public final String activateFunc;
    public final String derivativeFunc;

    StandardFunctions(String activateFunc, String derivativeFunc) {
        this.activateFunc = activateFunc;
        this.derivativeFunc = derivativeFunc;
    }

    public ActivationFunction get()  {
        return new ActivationFunction(activateFunc, derivativeFunc);
    }

    interface IFunction{
        double run(double value);
    }
}
