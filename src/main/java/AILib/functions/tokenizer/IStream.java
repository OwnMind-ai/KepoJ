package AILib.functions.tokenizer;

public interface IStream <T> {
    T next() throws Exception;
    T peek();
    boolean eof();
}
