package pl.parser.nbp.util;

import org.junit.Test;

import java.util.OptionalDouble;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author krzykrucz.
 */
public class MathUtilsTest {

    @Test
    public void stdDeviation() throws Exception {

        double[] doubles = {13.4, 2.5, 46.886};
        double expectedResult = 23.13093;

        OptionalDouble result = MathUtils.stdDeviation(doubles);

        assertTrue(result.isPresent());
        assertEquals(expectedResult, result.getAsDouble(), 0.0001d);

    }

}