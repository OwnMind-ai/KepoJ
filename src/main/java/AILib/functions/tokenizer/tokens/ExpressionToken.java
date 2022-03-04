package AILib.functions.tokenizer.tokens;

public class ExpressionToken implements IToken{
    public final String operator;
    public final IToken left;
    public final IToken right;

    public ExpressionToken(String operator, IToken left, IToken right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "ExpressionToken{" +
                "operator='" + operator + '\'' +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}
