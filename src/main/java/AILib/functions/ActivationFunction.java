package AILib.functions;

public interface ActivationFunction {
    double activate(double value);
    double derivative(double value);
}
