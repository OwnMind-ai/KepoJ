package AILib.functions;

public enum StandardFunctions {
    SIGMOID(ActivationFunction.create(
            (x) -> (1f/(1f + Math.pow(Math.E, -x))),
            (x) -> x * (1 - x))),

    TANH    (ActivationFunction.create(
            (x) -> ((Math.pow(Math.E, x) - Math.pow(Math.E, -x))/(Math.pow(Math.E, x) + Math.pow(Math.E, -x))),
            (x) -> (1 - Math.pow(x, 2)))),

    RELU   (ActivationFunction.create(
            (x) -> Math.max(0,x),
            (x) -> (x >= 0 ? 1 : 0))),

    LEAKY_RELU(ActivationFunction.create(
            (x) -> Math.max(0.01 * x,x),
            (x) -> (x >= 0 ? 1 : 0.01 * x))),

    BOUNDED_LEAKY_RELU (ActivationFunction.create(
            (x) -> (Math.min(1 + 0.01 * x, Math.max(0.01 * x, x))),
            (x) -> ((x <= 0 || x >= 1) ? 0.01f : 1))),

    IDENTICAL (ActivationFunction.create((x) -> x, (x) -> 1)),

    THRESHOLD(ActivationFunction.create(
            (x) -> x >= 0 ? 1 : 0,
            (x) -> x != 0 ? 0 : 1));

    private final ActivationFunction function;
    StandardFunctions(ActivationFunction function){
        this.function = function;
    }

    public ActivationFunction get(){
        return function;
    }
}
