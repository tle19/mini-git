package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Tyler Le
 */
class Arrays {

    /* C1. */

    /**
     * Returns a new array consisting of the elements of A followed by the
     * the elements of B.
     */
    static int[] catenate(int[] A, int[] B) {
        int[] result = new int[A.length + B.length];
        for (int i = 0; i < A.length; i++) {
            result[i] = A[i];
        }
        for (int i = 0; i < B.length; i++) {
            result[i + A.length] = B[i];
        }
        return result;
    }

    /* C2. */

    /**
     * Returns the array formed by removing LEN items from A,
     * beginning with item #START. If the start + len is out of bounds for our array, you
     * can return null.
     * Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     * result should be [0, 3].
     */
    static int[] remove(int[] A, int start, int len) {
        if (start + len > A.length) {
            return null;
        }
        int[] copy = new int[A.length - len];
        for (int i = 0; i < start; i++) {
            copy[i] = A[i];
            }
        for (int i = 0; i + start - 1 < A.length - len - 1; i++) {
            copy[i + start] = A[i + start + len];
        }
        return copy;
    }
}
