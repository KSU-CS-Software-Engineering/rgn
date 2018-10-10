package ksu.rgn.utils;


import java.util.Random;

/**
 *
 */
public class Collections {

    // Implementing Fisher–Yates shuffle
    public static void shuffleArray(Object[] array) {
        final Random r = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = r.nextInt(i + 1);
            // Simple swap
            Object a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

}
