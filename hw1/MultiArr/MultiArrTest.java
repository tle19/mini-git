import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] arr = {{1,3,4},{1},{5,6,7,8},{7,9}};
        assertEquals(9, MultiArr.maxValue(arr));
        int[][] arr2 = {{10, 20, 25},{3, 2, 1},{5, 6, 7},{100, 20, 2}};
        assertEquals(100, MultiArr.maxValue(arr2));
        int[][] arr3 = {{-1, -5, -2}, {-10, -15, -4}};
        assertEquals(-1, MultiArr.maxValue(arr3));
        int[][] arr4 = {{-1, -2, -3}, {-16, -14, -10}, {0, -2, -500}};
        assertEquals(0, MultiArr.maxValue(arr4));
    }

    @Test
    public void testAllRowSums() {
        int[][] arr = {{1,3,4},{1},{5,6,7,8},{7,9}};
        int[] arr_sum = {8,1,26,16};
        assertArrayEquals(arr_sum, MultiArr.allRowSums(arr));
        int[][] arr2 = {{10, 20, 25},{3, 2, 1},{5, 6, 7},{100, 20, 2}};
        int[] arr_sum2 = {55, 6, 18, 122};
        assertArrayEquals(arr_sum2, MultiArr.allRowSums(arr2));
        int[][] arr3 = {{-1, -5, -2}, {-10, -15, -4}};
        int[] arr_sum3 = {-8, -29};
        assertArrayEquals(arr_sum3, MultiArr.allRowSums(arr3));
        int[][] arr4 = {{-1, -2, -3}, {-16, -14, 10}, {-2, 249, -500}};
        int[] arr_sum4 = {-6, -20, -253};
        assertArrayEquals(arr_sum4, MultiArr.allRowSums(arr4));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
