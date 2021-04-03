package AILib.AILib;

import java.util.Arrays;

public class Dataset{
    private final double[][][] dataset;    //3D-array of dataset values

    public Dataset(double[][][] dataset){
        this.dataset = dataset;
    }    //Initialization by dataset array

    public Dataset(String fileName){                                    //Initialization by filepath
        double[] file = FileHandler.readFile(fileName);

        assert file != null : "";
        double[][][] result = new double[(int) file[0]][2][];

        //Wrapping a one-dimensional array into a three-dimensional one
        int counter = 3;    //Starts from index 3, 'cause previous indexes refer to array parameters values
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

    public void writeToFile(String fileName){      //Writes dataset to following filepath
        double[] datasetParameters = new double[]{
                this.dataset.length,
                this.dataset[0][0].length,
                this.dataset[0][1].length
        };

        //Cast three-dimensional [this.dataset] array to one-dimensional array
        double[] datasetArray = Arrays.stream(this.dataset).flatMap(Arrays::stream).flatMapToDouble(Arrays::stream).toArray();

        //Merging [datasetParameters] and [datasetArray]
        double[] result = Arrays.copyOf(datasetParameters, datasetArray.length + datasetParameters.length);
        System.arraycopy(datasetArray, 0, result, datasetParameters.length, datasetArray.length);

        FileHandler.writeToFile(result, fileName);
    }
}
