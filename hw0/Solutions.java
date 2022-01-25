/** Solutions to the HW0 Java101 exercises.
 *  @author Allyson Park and [Tyler Le]
 */
public class Solutions {

    /** Returns whether or not the input x is even.
     */
    public static boolean isEven(int x) {
        if (x % 2 == 0) {
            return true;
        }
        return false;
    }

    public static int max(int[] a) {
        int max = a[0];
        for (int k = 0; k < a.length; k++) {
            if (a[k] > max) {
                max = a[k];
            }
        }
        return max;
    }

    public static boolean wordBank(String word, String[] bank) {
        for (int k = 0; k < bank.length; k++) {
            if (word.equals(bank[k])) {
                return true;
            }
        }
        return false;
    }
    
    public static int threeSum(int[] a) {
        return 1;
    }

    // TODO: Fill in the method signatures for the other exercises
    // Your methods should be static for this HW. DO NOT worry about what this means.
    // Note that "static" is not necessarily a default, it just happens to be what
    // we want for THIS homework. In the future, do not assume all methods should be
    // static.

}
