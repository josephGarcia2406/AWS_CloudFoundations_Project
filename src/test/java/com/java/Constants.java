package com.java;

import java.util.Random;

public class Constants {

    public static String URL = "http://ec2-3-90-29-68.compute-1.amazonaws.com";
    public static Random random;

    static {
        random = new Random();
    }

    public static int getRandomId() {
        return random.nextInt(1000000);
    }

    public static int getRandomHoras() {
        return random.nextInt(50);
    }

    public static double getPromedio() {
        return Math.round(random.nextDouble() * 100d) / 100d;
    }
}
