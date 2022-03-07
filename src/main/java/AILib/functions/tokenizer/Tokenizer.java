package AILib.functions.tokenizer;

import AILib.functions.parser.Parser;
import AILib.functions.tokenizer.tokens.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Tokenizer implements IStream<IToken>{
    private static final String whitespaces = " \n\t";
    private static final String[] operators = {"+", "-", "*", "/", "%", "**"};
    private static final String digits = "-0123456789";
    private static final String punctuation = ",()";
    private static final String letters = "abcdefghijklmnopqrstuvwxyz";

    private final CharsStream charsStream;
    private IToken current;

    public Tokenizer(CharsStream charsStream) {
        this.charsStream = charsStream;
    }

    private static boolean isDigit(char ch) { return Tokenizer.digits.indexOf(ch) > -1; }
    private static boolean isIdStart(char ch) { return Tokenizer.letters.indexOf(ch) > -1; }
    private static boolean isOperator(char ch) {return Arrays.asList(Tokenizer.operators).contains(String.valueOf(ch));}
    private static boolean isWhitespace(char ch) { return Tokenizer.whitespaces.indexOf(ch) > -1; }
    private static boolean isPunctuation(char ch) { return Tokenizer.punctuation.indexOf(ch) > -1; }
    private static boolean isId(char ch) {return Tokenizer.isIdStart(ch) || (Tokenizer.digits + "_").indexOf(ch) > -1;}

    private IToken readNext() throws Exception {
        this.skipWhitespaces();
        if (this.charsStream.eof()) return null;

        char ch = this.charsStream.peek();

        if(String.copyValueOf(new char[]{ch}).equals("-")){
            Character previous = this.charsStream.previous(whitespaces);
            System.out.println(previous);
            if (previous == null || previous == Parser.delimiterStart.punc)
                 return this.readNumber();
            else return this.readOperator();
        }

        if(isDigit(ch))
            return this.readNumber();

        if(isOperator(ch))
            return this.readOperator();

        if(isPunctuation(ch))
            return new PunctuationToken(this.charsStream.next());

        if(isIdStart(ch))
            return this.readIdentification();

        this.throwError("Cant read data");
        return null;
    }

    private IToken readOperator() throws Exception {
        int maxLenOperator = 2;

        for (int i = maxLenOperator ; i > 0; i--) {

            String operatorInCode = this.charsStream.peek(i);

            if (Arrays.asList(Tokenizer.operators).contains(operatorInCode)) {
                return new OperatorToken(this.charsStream.next(i));
            }
        }

        throwError("Cant find operator");
        return null;
    }

    private IToken readIdentification(){
        String name = this.readWhile(Tokenizer::isId);

        return new NameToken(name);
    }

    private IToken readNumber() throws TokenizeException {
        String num = readWhile(x -> Tokenizer.isDigit(x) || x == '.');

        if (num.chars().filter(ch -> ch == '.').count() > 1) {
            this.throwError("There is more than one dot in number");
        }

        return new NumberToken(Double.parseDouble(num));
    }

    private void skipWhitespaces() {
        this.readWhile(Tokenizer::isWhitespace);
    }

    private String readWhile(IReadWhile predicate) {
        StringBuilder text = new StringBuilder();

        while (!this.charsStream.eof() && predicate.check(this.charsStream.peek())) {
            text.append(this.charsStream.next());
        }

        return text.toString();
    }

    private void throwError(String msg) throws TokenizeException {
        throw new TokenizeException(msg, charsStream.getColumn());
    }

    @Override
    public IToken next() throws Exception {
        IToken tok = this.current;
        this.current = null;
        return tok == null ? this.readNext() : tok;
    }

    @Override
    public IToken peek() {
        if (this.current == null) {
            try {
                this.current = this.readNext();
            } catch (Exception ignored) {
                return this.current;
            }
        }
        return this.current;
    }

    @Override
    public boolean eof() {
        return this.charsStream.eof();
    }

    public IToken[] getAll() throws Exception {
        ArrayList<IToken> tokens = new ArrayList<>();

        while(!this.eof()) {
            tokens.add(this.next());
        }

        return tokens.toArray(new IToken[0]);
    }

    private interface IReadWhile {
        boolean check(char peek);
    }
}