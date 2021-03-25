package AILib.AILib;

import java.util.Arrays;

public class Dataset{
    private final double[][][] dataset;

    public Dataset(double[][][] dataset){
        this.dataset = dataset;
    }

    public Dataset(String fileName){
        double[] file = FileHandler.readFile(fileName);

        assert file != null : "";
        double[][][] result = new double[(int) file[0]][2][];

        int counter = 3;
        for (int x = 0; x < result.length; x++) {
            for (int y = 0; y < result[x].length; y++) {
                result[x][y] = new double[(int) file[y + 1]];
                for (int z = 0; z < result[x][y].length; z++)
                    result[x][y][z] = file[counter++];
            }
        }
        this.dataset = result;
    }

    public double[][][] getDatasetArray(){
        return this.dataset;
    }

    public void writeToFile(String fileName){
        double[] datasetParameters = new double[]{
                this.dataset.length,
                this.dataset[0][0].length,
                this.dataset[0][1].length
        };
        double[] datasetArray = Arrays.stream(this.dataset).flatMap(Arrays::stream).flatMapToDouble(Arrays::stream).toArray();

        double[] result = Arrays.copyOf(datasetParameters, datasetArray.length + datasetParameters.length);
        System.arraycopy(datasetArray, 0, result, datasetParameters.length, datasetArray.length);
        FileHandler.writeToFile(result, fileName);
    }
}
