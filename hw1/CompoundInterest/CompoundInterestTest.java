import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(0, CompoundInterest.numYears(2022));
        assertEquals(1, CompoundInterest.numYears(2023));
        assertEquals(100, CompoundInterest.numYears(2122));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2024), tolerance);
        assertEquals(83.923, CompoundInterest.futureValue(22, 25, 2028), tolerance);
        assertEquals(8.800, CompoundInterest.futureValue(10, -12, 2023), tolerance);
        assertEquals(3.915, CompoundInterest.futureValue(22, -25, 2028), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
