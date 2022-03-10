package AILib.functions.compiler;

import AILib.functions.parser.Parser;
import AILib.functions.tokenizer.tokens.*;

import java.util.Arrays;

public class Compiler {
    private static final String exponent = "e";
    private static final String pi = "pi";
    private static final String argument = "x";

    private final Parser parser;

    public Compiler(Parser parser) {
        this.parser = parser;
    }

    public ElementaryFunction compile() throws Exception {
        return this.buildFunctionsTree(this.parser.parse());
    }

    private ElementaryFunction buildFunctionsTree(ExpressionToken tokensTree) throws CompileException {
        ElementaryFunction myFunc = this.searchFunction(tokensTree.operator.operator);

        ElementaryFunction left = this.findFunction(tokensTree.left);
        ElementaryFunction right = this.findFunction(tokensTree.right);

        assert myFunc != null;
        return args -> myFunc.run(left.run(args[0]), right.run(args[0]));
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
        if(token instanceof NumberToken) return args -> (((NumberToken) token).value);
        else if (token instanceof NameToken){
            NameToken nameToken = (NameToken) token;
            switch (nameToken.name) {
                case argument:
                    return args -> args[0];
                case Compiler.exponent:
                    return args -> Math.E;
                case Compiler.pi:
                    return args -> Math.PI;
                default:
                    throw new CompileException("Undefined argument", nameToken);
            }
        }
        else if(token instanceof CallToken){
            CallToken callToken = (CallToken) token;
            ElementaryFunction function = this.searchFunction(callToken.name);

            ElementaryFunction[] arguments = new ElementaryFunction[callToken.args.length];
            for (int i = 0; i < callToken.args.length; i++)
                arguments[i] = this.findFunction(callToken.args[i]);


            assert function != null;
            return args -> function.run(
                    Arrays.stream(arguments).mapToDouble(n -> n.run(args[0])).toArray()
            );
        }
        throw new CompileException("Undefined token", token);
    }
}
