package AILib.functions.parser;

import AILib.functions.tokenizer.CharsStream;
import AILib.functions.tokenizer.Tokenizer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parse() throws Exception {
        String code = "-3 - (-1 + 1) + 5 - sqrt() - 2";
        System.out.println(Arrays.toString(new Tokenizer(new CharsStream(code)).getAll()));
        Parser parser = new Parser(new Tokenizer(new CharsStream(code)));
        System.out.println(parser.parse());
    }
}