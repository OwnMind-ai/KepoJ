package AILib.functions.compiler;

public enum ElementaryFunctions {
    SUM(args -> args[0] + args[1], "+"),
    SUBTRACT(args -> args[0] - args[1], "-"),
    MULTIPLICATION(args -> args[0] * args[1], "*"),
    DIVISION(args -> args[0] / args[1], "/"),
    POW(args -> Math.pow(args[0], args[1]), "**"),
    MOD(args -> args[0] % args[1], "%"),
    FLOOR_DIV(args -> Math.floorDiv((int) args[0], (int) args[1]), "//"),

    SQRT(args -> Math.sqrt(args[0]), "sqrt"),
    MIN(args -> Math.min(args[0], args[1]), "min"),
    MAX(args -> Math.max(args[0], args[1]), "max"),
    SIN(args -> Math.sin(args[0]), "sin"),
    COS(args -> Math.cos(args[0]), "cos"),
    TAN(args -> Math.tan(args[0]), "tan"),
    ASIN(args -> Math.asin(args[0]), "asin"),
    ACOS(args -> Math.acos(args[0]), "acos"),
    ATAN(args -> Math.atan(args[0]), "atan"),
    ROUND(args -> Math.round(args[0]), "round"),
    FLOOR(args -> Math.floor(args[0]), "floor"),
    CEIL(args -> Math.ceil(args[0]), "ceil"),
    RANDOM(args -> Math.random(), "random");

    public final ElementaryFunction function;
    public final String operator;

    ElementaryFunctions(ElementaryFunction function, String operator) {
        this.function = function;
        this.operator = operator;
    }

    public double run(double x, double y) { return function.run(x, y); }
}
