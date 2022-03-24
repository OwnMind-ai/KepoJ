package AILib.entities;

import java.io.*;
import java.util.Arrays;

public class Dataset implements Serializable {
    private final double[][][] dataset;

    public Dataset(double[][][] dataset){
        this.dataset = dataset;
    }

    public static Dataset read(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(fileName));
        Dataset result = (Dataset) stream.readObject();
        stream.close();

        return result;
    }

    public double[][][] toArray(){
        return this.dataset;
    }

    public void write(String fileName) throws IOException{
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fileName));
        stream.writeObject(this);
        stream.close();
    }

    @Override
    public String toString() {
        return "Dataset{" + Arrays.deepToString(dataset) + '}';
    }
}
