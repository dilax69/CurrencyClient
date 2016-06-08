package pl.parser.nbp.util;

import java.util.Arrays;
import java.util.OptionalDouble;

/**
 * @author krzykrucz.
 */
public final class MathUtils {

    public static OptionalDouble stdDeviation(double... doubles) {

        int n = doubles.length;
        if (n <= 1) return OptionalDouble.empty();

        double sum = Arrays.stream(doubles).sum();
        double sumOfSquares = Arrays.stream(doubles).map(d -> d * d).sum();

        double variance = (n * sumOfSquares - sum * sum) / (n * (n - 1));

        return OptionalDouble.of(Math.sqrt(variance));
    }


}
