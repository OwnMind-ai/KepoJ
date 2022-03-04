package AILib.functions.tokenizer;

import AILib.functions.tokenizer.tokens.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    private static final String code = "  12 + 3.5 * x ** 2 - sqrt(9, 2)";
    private static final IToken[] result = {new NumberToken(12), new OperatorToken("+"),
            new NumberToken(3.5), new OperatorToken("*"),
            new NameToken("x"), new OperatorToken("**"),
            new NumberToken(2), new OperatorToken("-"),
            new NameToken("sqrt"), new PunctuationToken("("),
            new NumberToken(9), new PunctuationToken(","),
            new NumberToken(2), new PunctuationToken(")")};

    @Test
    void next() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new CharsStream(code));

        for (IToken token : result) {
            assertEquals(tokenizer.next(), token);
        }
    }

    @Test
    void peek() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new CharsStream(code));

        for (IToken token : result) {
            assertEquals(tokenizer.peek(), token);
            tokenizer.next();
        }
    }

    @Test
    void eof() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new CharsStream("test"));
        assertFalse(tokenizer.eof());
        tokenizer.next();
        assertTrue(tokenizer.eof());
    }

    @Test
    void getAll() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new CharsStream(code));
        assertArrayEquals(result, tokenizer.getAll());
    }
}