package AILib.AILib;

import java.io.*;

public class FileHandler {
    public static void writeToFile(double[] array, String fileName){
        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
            dos.writeInt(array.length);
            for (double value : array)
                dos.writeDouble(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[] readFile(String fileName){
        try(DataInputStream dos = new DataInputStream(new FileInputStream(fileName))) {
            double [] result = new double[dos.readInt()];
            for(int i = 0; i < result.length; i++)
                result[i] = dos.readDouble();

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
