package AILib.functions.compiler;

import AILib.functions.ActivationFunction;
import AILib.functions.parser.Parser;
import AILib.functions.tokenizer.CharsStream;
import AILib.functions.tokenizer.Tokenizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {

    @Test
    void compile() throws Exception {
        Compiler compiler = new Compiler(
                new Parser(new Tokenizer(new CharsStream("12 * x + 3 - max(sqrt(18), x)")))
        );

        ElementaryFunction f = compiler.compile();
        System.out.println(f.run(3));
    }
}