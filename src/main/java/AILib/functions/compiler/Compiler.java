package AILib.functions.compiler;

import AILib.functions.ActivationFunction;
import AILib.functions.parser.Parser;
import AILib.functions.tokenizer.tokens.*;

public class Compiler {
    public static final String argument = "x";

    private final Parser activation;
    private final Parser derivate;

    public Compiler(Parser activation, Parser derivate) {
        this.activation = activation;
        this.derivate = derivate;
    }

    public ActivationFunction compile() throws Exception {
        ElementaryFunction activation = this.buildFunctionsTree(this.activation.parse());
        ElementaryFunction derivative = this.buildFunctionsTree(this.derivate.parse());

        return new ActivationFunction() {
            @Override
            public double activate(double value) {
                return activation.run(value, value);
            }

            @Override
            public double derivative(double value) {
                return derivative.run(value, value);
            }
        };
    }

    private ElementaryFunction buildFunctionsTree(ExpressionToken tokensTree) throws CompileException {
        ElementaryFunction myFunc = this.searchFunction(tokensTree.operator.operator);

        ElementaryFunction left = this.findFunction(tokensTree.left);
        ElementaryFunction right = this.findFunction(tokensTree.right);

        assert myFunc != null;
        return (x, y) -> myFunc.run(left.run(x, x), right.run(x, x));
    }

    private ElementaryFunction searchFunction(String key){
        for (ElementaryFunctions f : ElementaryFunctions.values())
            if (f.operator.equals(key)) return f.function;
        return null;
    }

    private ElementaryFunction findFunction(IToken token) throws CompileException {
        return token instanceof ExpressionToken ?
                buildFunctionsTree((ExpressionToken) token) :
                this.getTokenFunction(token);
    }

    private ElementaryFunction getTokenFunction(IToken token) throws CompileException {
        if(token instanceof NumberToken) return (x, y) -> (((NumberToken) token).value);
        else if (token instanceof NameToken){
            NameToken nameToken = (NameToken) token;
            if(nameToken.name.equals(argument)) return (x, y) -> x;
            else throw new CompileException("Undefined argument", nameToken);
        }
        else if(token instanceof CallToken){
            CallToken callToken = (CallToken) token;
            ElementaryFunction function = this.searchFunction(callToken.name);

            // TODO: use dynamic arguments at ElementaryFunction interface
            ElementaryFunction first = this.findFunction(callToken.args[0]);
            ElementaryFunction second = callToken.args.length == 2 ? this.findFunction(callToken.args[1]) :
                    (x, y) -> x;

            assert function != null;
            return (x, y) -> function.run(first.run(x, x), second.run(x, x));
        }
        throw new CompileException("Undefined token", token);
    }
}
