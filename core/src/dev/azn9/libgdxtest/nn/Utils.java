package dev.azn9.libgdxtest.nn;

import java.util.function.Function;

public class Utils {

    public static final Function<Float, Float> SIGMOID = x -> 1 / (1 + (float) Math.exp(-10 * x + 5));

}
