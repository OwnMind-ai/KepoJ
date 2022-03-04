package AILib.functions.tokenizer.tokens;

import java.util.Objects;

public class NameToken implements IToken{
    public final String name;

    public NameToken(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NameToken{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameToken nameToken = (NameToken) o;
        return Objects.equals(name, nameToken.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
