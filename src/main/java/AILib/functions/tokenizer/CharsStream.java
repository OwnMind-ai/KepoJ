package AILib.functions.tokenizer;

public class CharsStream implements IStream<Character>{
    private final char[] source;
    private int index = 0;

    public CharsStream(String source){
        this.source = source.toCharArray();
    }

    @Override
    public Character next(){
        return this.source[this.index++];
    }

    public String next(int length){
        String result = peek(length);
        this.index+= length;

        return result;
    }

    @Override
    public Character peek(){
        return this.source[this.index];
    }

    public String peek(int length){
        return String.copyValueOf(this.source).substring(this.index, this.index + length);
    }

    @Override
    public boolean eof(){
        return this.index >= this.source.length;
    }

    public Character previous(String skip){
        for (int i = this.index - 1; i >= 0; i--)
            if (skip.indexOf(this.source[i]) <= -1) return this.source[i];

        return null;
    }

    public int getColumn(){
        return this.index;
    }

    @Override
    public String toString(){
        return String.copyValueOf(this.source);
    }
}
