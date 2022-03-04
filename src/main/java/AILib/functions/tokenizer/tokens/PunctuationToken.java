package AILib.functions.tokenizer.tokens;

import java.util.Objects;

public class PunctuationToken implements IToken{
    public final char punc;

    public PunctuationToken(String punc) {
        this.punc = punc.toCharArray()[0];
    }

    public PunctuationToken(char punc){
        this.punc = punc;
    }

    @Override
    public String toString() {
        return "PunctuationToken{" +
                "punc='" + punc + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PunctuationToken that = (PunctuationToken) o;
        return punc == that.punc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(punc);
    }
}
