package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {

    @Test
    public void catenateTest() {
        int[] a = {1, 2, 3, 4};
        int[] b = {5, 6, 7, 8};
        int[] c = {1, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(c, Arrays.catenate(a, b));

        int[] d = {2, 1, 3};
        int[] e = {9, 2, 1, 5, 7};
        int[] f = {2, 1, 3, 9, 2, 1, 5, 7};
        assertArrayEquals(f, Arrays.catenate(d, e));
    }

    @Test
    public void removeTest() {
        int[] a = {1, 2, 3, 4};
        int[] b = {1, 4};
        assertArrayEquals(b, Arrays.remove(a, 1, 2));

        int[] c = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] d = {1, 2, 3, 8, 9};
        assertArrayEquals(d, Arrays.remove(c, 3, 4));
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
