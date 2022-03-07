package AILib.functions.compiler;

public enum ElementaryFunctions {
    SUM(Double::sum),
    ;

    public final ElementaryFunction function;

    ElementaryFunctions(ElementaryFunction function) {
        this.function = function;
    }

    public double run(double x, double y) { return function.run(x, y); }
}
