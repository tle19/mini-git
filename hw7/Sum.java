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
        Arrays.sort(A);
        Arrays.sort(B);
        for (int i: A) {
            for (int j: B) {
                if (i + j == m) {
                    return true;
                }
            }
        }
        return false;
    }
}
