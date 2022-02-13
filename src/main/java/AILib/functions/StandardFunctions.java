package AILib.functions;

public enum StandardFunctions {
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

    THRESHOLD((x) -> x >= 0 ? 1 : 0,
            (x) -> x != 0 ? 0 : 1);

    public final ActivationFunction function;

    StandardFunctions(IFunction activateFunc, IFunction derivativeFunc){
        this.function = new ActivationFunction() {
            @Override
            public double activate(double value) {
                return activateFunc.run(value);
            }

            @Override
            public double derivative(double value) {
                return derivativeFunc.run(value);
            }
        };
    }

    public ActivationFunction get(){ return this.function; }

    interface IFunction{
        double run(double value);
    }
}
