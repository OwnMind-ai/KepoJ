package AILib.utills;

import java.io.*;

public class FileHandler {
    //Writes double[] array to filepath
    public static void writeToFile(double[] array, String fileName){
        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
            dos.writeInt(array.length);   //Writes array parameters for future reading
            for (double value : array)
                dos.writeDouble(value);
        } catch (IOException e) {
            System.out.println(fileName + " (No such file or directory)");
        }
    }

    //Read file and returns double[] array, that written to a file
    public static double[] readFile(String fileName){
        try(DataInputStream dos = new DataInputStream(new FileInputStream(fileName))) {
            double [] result = new double[dos.readInt()];   //Creating array by parameters in file
            for(int i = 0; i < result.length; i++)
                result[i] = dos.readDouble();

            return result;
        } catch (IOException e) {
            System.out.println(fileName + " (No such file or directory)");
        }
        return null;
    }
}
