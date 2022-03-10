package AILib.functions.compiler;

public enum ElementaryFunctions {
    SUM(args -> args[0] + args[1], "+"),
    SUBTRACT(args -> args[0] - args[1], "-"),
    MULTIPLICATION(args -> args[0] * args[1], "*"),
    DIVISION(args -> args[0] / args[1], "/"),
    POW(args -> Math.pow(args[0], args[1]), "**"),
    SQRT(args -> Math.sqrt(args[0]), "sqrt"),
    MIN(args -> Math.min(args[0], args[1]), "min"),
    MAX(args -> Math.max(args[0], args[1]), "max");

    public final ElementaryFunction function;
    public final String operator;

    ElementaryFunctions(ElementaryFunction function, String operator) {
        this.function = function;
        this.operator = operator;
    }

    public double run(double x, double y) { return function.run(x, y); }
}
