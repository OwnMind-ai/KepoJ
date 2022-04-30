package AILib.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Dataset implements Serializable {
    private final LinkedList<double[][]> dataset;

    public Dataset(double[][][] dataset){
        this.dataset = new LinkedList<>();
        Collections.addAll(this.dataset, dataset);
    }

    public void add(double[] input, double[] expected){
        this.dataset.add(new double[][]{input, expected});
    }

    public double[][] get(int index){
        return this.dataset.get(index);
    }

    public double[][] remove(int index){
        return this.dataset.remove(index);
    }

    public int size(){
        return this.dataset.size();
    }

    public static Dataset read(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
        Dataset result = (Dataset) stream.readObject();
        stream.close();

        return result;
    }

    public double[][][] toArray(){
        return this.dataset.toArray(new double[0][][]);
    }

    public void write(String fileName) throws IOException{
        ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)));
        stream.writeObject(this);
        stream.close();
    }

    @Override
    public String toString() {
        return "Dataset{" + dataset + '}';
    }
}
