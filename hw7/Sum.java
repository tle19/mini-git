/** HW #7, Two-sum problem.
 * @author Tyler Le
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B.length; j++) {
                if (A[i] + B[j] == m) {
                    return true;
                }
            }
        }
        return false;
    }
}
