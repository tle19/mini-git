package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author Tyler Le
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

        int[] g = {1, 2, 3, 4};
        int[] h = {};
        int[] i = {1, 2, 3, 4};
        assertArrayEquals(i, Arrays.catenate(g, h));

        int[] j = {};
        int[] k = {};
        int[] l = {};
        assertArrayEquals(l, Arrays.catenate(j, k));
    }

    @Test
    public void removeTest() {
        int[] a = {1, 2, 3, 4};
        int[] b = {1, 4};
        assertArrayEquals(b, Arrays.remove(a, 1, 2));

        int[] c = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] d = {1, 2, 3, 8, 9};
        assertArrayEquals(d, Arrays.remove(c, 3, 4));

        int[] e = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] f = {};
        assertArrayEquals(f, Arrays.remove(e, 0, 9));

        int[] g = {};
        int[] h = {};
        assertArrayEquals(h, Arrays.remove(g, 0, 0));

        int[] i = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] j = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(j, Arrays.remove(i, 5, 0));
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
