package AILib.functions.tokenizer.tokens;

import java.util.Objects;

public class OperatorToken implements IToken{
    public final String operator;

    public OperatorToken(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "OperatorToken{" +
                "operator='" + operator + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperatorToken that = (OperatorToken) o;
        return Objects.equals(operator, that.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator);
    }
}
