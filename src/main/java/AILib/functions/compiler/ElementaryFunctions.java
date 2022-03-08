package AILib.functions.compiler;

public enum ElementaryFunctions {
    SUM(Double::sum, "+"),
    SUBTRACT((x, y) -> x - y, "-"),
    MULTIPLICATION((x, y) -> x * y, "*"),
    DIVISION((x, y) -> x / y, "/"),
    POW(Math::pow, "**"),
    SQRT((x, y) -> Math.sqrt(x), "sqrt"),;

    public final ElementaryFunction function;
    public final String operator;

    ElementaryFunctions(ElementaryFunction function, String operator) {
        this.function = function;
        this.operator = operator;
    }

    public double run(double x, double y) { return function.run(x, y); }
}
