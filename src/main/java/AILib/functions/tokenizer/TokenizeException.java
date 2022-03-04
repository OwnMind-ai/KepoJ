package AILib.functions.tokenizer;

public class TokenizeException extends Exception {
    public final int column;
    private String formatPosition = "(%d:%d)";

    public TokenizeException(String msg, String format, int column) {
        this(msg, column);
        this.formatPosition = format;
    }

    public TokenizeException(String msg, int column) {
        super(msg);
        this.column = column;
    }

    @Override
    public String toString() {
        return super.getMessage() + " " + String.format(formatPosition, column);
    }
}