package AILib.functions.compiler;

import AILib.functions.parser.Parser;
import AILib.functions.tokenizer.CharsStream;
import AILib.functions.tokenizer.Tokenizer;
import org.junit.jupiter.api.Test;

class CompilerTest {

    @Test
    void compile() throws Exception {
        Compiler compiler = new Compiler(
                new Parser(new Tokenizer(new CharsStream("min(1 + 0.01 * x, max(0.01 * x, x))")))
        );

        ElementaryFunction f = compiler.compile();
        for (float i = -5; i < 5; i+= 0.5)
            System.out.println(f.run(i));
    }
}