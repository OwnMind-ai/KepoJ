package AILib.functions.tokenizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CharsStreamTest {

    @Test
    void next() {
        String text = "some text";
        CharsStream charsStream = new CharsStream("some text");

        for (char c : text.toCharArray())
            assertEquals(c, charsStream.next());

        charsStream = new CharsStream("some text");
        assertEquals(text, charsStream.next(text.length()));
    }

    @Test
    void eof() {
        CharsStream charsStream = new CharsStream("1");
        assertFalse(charsStream.eof());
        charsStream.next();
        assertTrue(charsStream.eof());
    }

    @Test
    void testToString() {
        String text = "some text";
        CharsStream charsStream = new CharsStream("some text");
        assertEquals(text, charsStream.toString());
    }
}