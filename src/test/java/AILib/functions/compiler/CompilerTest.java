package AILib.functions.compiler;

import AILib.functions.parser.Parser;
import AILib.functions.tokenizer.CharsStream;
import AILib.functions.tokenizer.Tokenizer;
import org.junit.jupiter.api.Test;

class CompilerTest {

    @Test
    void compile() throws Exception {
        String code = "if(x % 2 == 0, -13, -e * (-x))";
        Compiler compiler = new Compiler(
                new Parser(new Tokenizer(new CharsStream(code)))
        );
        // TODO: Error messages
        System.out.println( new Parser(new Tokenizer(new CharsStream(code))).parse());
        ElementaryFunction f = compiler.compile();
        for (float i = -5; i < 5; i+= 0.5)
            System.out.println(i + " " + f.run(i));
    }
}