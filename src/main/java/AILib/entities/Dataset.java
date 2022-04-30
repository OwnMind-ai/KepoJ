package AILib.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Dataset implements Serializable {
    private final double[][][] dataset;

    public Dataset(double[][][] dataset){
        this.dataset = dataset;
    }

    public static Dataset read(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
        Dataset result = (Dataset) stream.readObject();
        stream.close();

        return result;
    }

    public double[][][] toArray(){
        return this.dataset;
    }

    public void write(String fileName) throws IOException{
        ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)));
        stream.writeObject(this);
        stream.close();
    }

    @Override
    public String toString() {
        return "Dataset{" + Arrays.deepToString(dataset) + '}';
    }
}
