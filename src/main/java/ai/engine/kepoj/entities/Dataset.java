package ai.engine.kepoj.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Dynamic data container that contains a bunch of input and expected output values
 * @since 1.1
 */
public class Dataset implements Serializable {
    private final LinkedList<double[][]> dataset;

    /**
     * Creates dataset from doubles array
     * @since 1.1
     */
    public Dataset(double[][][] dataset){
        this.dataset = new LinkedList<>();
        Collections.addAll(this.dataset, dataset);
    }

    public Dataset(){
        this.dataset = new LinkedList<>();
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

    public Dataset concat(Dataset other){
        return new Dataset(Stream.concat(this.dataset.stream(), other.dataset.stream())
                .toArray(double[][][]::new));
    }

    public int size(){
        return this.dataset.size();
    }

    /**
     * Reads a serialized dataset class from a file
     * @param fileName path to file
     * @throws IOException file not found
     * @throws ClassNotFoundException can't read dataset class from file
     */
    public static Dataset read(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
        Dataset result = (Dataset) stream.readObject();
        stream.close();

        return result;
    }

    public double[][][] toArray(){
        return this.dataset.toArray(new double[0][][]);
    }

    /** Writes a dataset class as binary
     * @param fileName path to file
     * @throws IOException file not found
     */
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
