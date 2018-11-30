package ksu.rgn.utils;

/**
 *
 */
public class Units {

    public static int toKilograms(int pounds) {
        return (int)(pounds / 2.204623);
    }

    public static int toPounds(int kilograms) {
        return (int)(kilograms * 2.204623);
    }

}
