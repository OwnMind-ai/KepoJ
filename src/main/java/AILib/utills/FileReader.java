package AILib.utills;

import AILib.functions.tokenizer.IStream;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReader implements IStream<Double> {
    private final DataInputStream dos;
    private Double current;

    public FileReader(String filename) throws IOException {
        this.dos = new DataInputStream(new FileInputStream(filename));
        this.next();
    }

    @Override
    public Double next() {
        Double result = this.current;

        try {
            this.current = this.dos.readDouble();
        } catch (IOException e) {
            this.current = null;
        }

        return result;
    }

    @Override
    public Double peek() {
        return this.current;
    }

    @Override
    public boolean eof() {
        try {
            return this.dos.read() == -1;
        } catch (IOException ignored) {}
        return false;
    }
}
