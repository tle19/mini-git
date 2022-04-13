import java.util.Arrays;

/** HW #7, Two-sum problem.
 * @author Tyler Le
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        if (A == null || B == null) {
            return false;
        }
        for (int i = 0; i < B.length; i++) {
            int searched = Arrays.binarySearch(A, m - B[i]);
            if (searched >= 0 && searched < A.length && A[searched] + B[i] == m) {
                return true;
            }
        }
        return false;
    }
}
