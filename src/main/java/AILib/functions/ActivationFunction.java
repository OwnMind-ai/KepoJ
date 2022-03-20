package AILib.functions;

import AILib.functions.compiler.ElementaryFunction;
import AILib.functions.parser.Parser;
import AILib.functions.tokenizer.CharsStream;
import AILib.functions.tokenizer.Tokenizer;
import AILib.functions.compiler.Compiler;

import java.util.Objects;

public class ActivationFunction {
    private final String activationCode;
    private final String derivativeCode;

    private final ElementaryFunction activateFunction;
    private final ElementaryFunction derivativeFunction;

    public ActivationFunction(String activate, String derivative) {
        this.activateFunction = new Compiler(new Parser(new Tokenizer(new CharsStream(
                activate
        )))).compile();

        this.derivativeFunction = new Compiler(new Parser(new Tokenizer(new CharsStream(
                derivative
        )))).compile();

        this.activationCode = activate;
        this.derivativeCode = derivative;
    }

    public double activate(double value){ return this.activateFunction.run(value); }
    public double derivative(double value){ return this.derivativeFunction.run(value); }

    public String getActivate(){ return this.activationCode; }
    public String getDerivative(){ return this.derivativeCode; }

    @Override
    public int hashCode() {
        return Objects.hash(activationCode, derivativeCode);
    }
}
