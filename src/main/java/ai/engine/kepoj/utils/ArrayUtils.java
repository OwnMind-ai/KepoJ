package ai.engine.kepoj.utils;

public class ArrayUtils {
    public static int getMaxIndex(double[] array){
        double max = Double.NEGATIVE_INFINITY;
        int id = -1;

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max){
                max = array[i];
                id = i;
            }
        }

        return id;
    }
}
