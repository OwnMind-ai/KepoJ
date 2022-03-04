package AILib.functions.tokenizer.tokens;

import java.util.Arrays;

public class CallToken implements IToken{
    public final String name;
    public final IToken[] args;

    public CallToken(String name, IToken[] args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public String toString() {
        return "CallToken{" +
                "name='" + name + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
