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
        assertEquals(11.8026496, CompoundInterest.futureValueReal(10, 12, 2024, 3), tolerance);
        assertEquals(69.9132562, CompoundInterest.futureValueReal(22, 25, 2028, 3), tolerance);
        assertEquals(7.2863296, CompoundInterest.futureValueReal(10, -12, 2024, 3), tolerance);
        assertEquals(13.990348, CompoundInterest.futureValueReal(22, -6, 2026, 5), tolerance);
        assertEquals(23.0280414, CompoundInterest.futureValueReal(15, 12, 2025, -3), tolerance);
        assertEquals(5.01854331, CompoundInterest.futureValueReal(12, -20, 2027, -5), tolerance);
    }

    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(1000, CompoundInterest.totalSavings(1000, 2022, 5), tolerance);
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2024, 10), tolerance);
        assertEquals(5765.625, CompoundInterest.totalSavings(1000, 2025, 25), tolerance);
        assertEquals(52040.4016, CompoundInterest.totalSavings(10000, 2026, 2), tolerance);
        assertEquals(14702, CompoundInterest.totalSavings(5000, 2024, -2), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(1000, CompoundInterest.totalSavingsReal(1000, 2022, 5, 3), tolerance);
        assertEquals(15571.895, CompoundInterest.totalSavingsReal(5000, 2024, 10, 3), tolerance);
        assertEquals(4943.30273, CompoundInterest.totalSavingsReal(1000, 2025, 25, 5), tolerance);
        assertEquals(58571.9305, CompoundInterest.totalSavingsReal(10000, 2026, 2, -3), tolerance);
        assertEquals(15901.6832, CompoundInterest.totalSavingsReal(5000, 2024, -2, -4), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
