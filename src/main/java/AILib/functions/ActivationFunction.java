package AILib.functions;

import AILib.functions.compiler.ElementaryFunction;
import AILib.functions.parser.Parser;
import AILib.functions.tokenizer.CharsStream;
import AILib.functions.tokenizer.Tokenizer;
import AILib.functions.compiler.Compiler;

public interface ActivationFunction {
    double activate(double value);
    double derivative(double value);

    static ActivationFunction generate(String activate, String derivative) throws Exception {
        ElementaryFunction activateFunction = new Compiler(new Parser(new Tokenizer(new CharsStream(
                activate
        )))).compile();

        ElementaryFunction derivativeFunction = new Compiler(new Parser(new Tokenizer(new CharsStream(
                derivative
        )))).compile();

        return new ActivationFunction() {
            @Override
            public double activate(double value){
                return activateFunction.run(value);
            }

            @Override
            public double derivative(double value) {
                return derivativeFunction.run(value);
            }
        };
    }
}
