package AILib.functions.compiler;

import AILib.functions.tokenizer.tokens.IToken;

public class CompileException extends Exception {
    public final IToken token;

    public CompileException(String msg, IToken token) {
        super(msg);
        this.token = token;
    }

    @Override
    public String toString() {
        return super.getMessage() + " " + token;
    }
}
