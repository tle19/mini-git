/** Multidimensional array
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
     * {{"hello","you","world"} ,{"how","are","you"}} prints:
     * Rows: 2
     * Columns: 3
     * <p>
     * {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
     * Rows: 4
     * Columns: 4
     */
    public static void printRowAndCol(int[][] arr) {
        int row_count = arr.length;
        int col_count = 0;
        for (int i = 0; i < row_count; i++) {
            if (arr[i].length > col_count) {
                col_count = arr[i].length;
            }
        }
        System.out.println("Rows: " + row_count);
        System.out.println("Columns: " + col_count);
    }

    /**
     * @param arr: 2d array
     * @return maximal value present anywhere in the 2d array
     */
    public static int maxValue(int[][] arr) {
        int max = arr[0][0];
        for (int i = 0; i < arr.length; i++) {
            for (int k = 0; k < arr[i].length; k++) {
                if (arr[i][k] > max) {
                    max = arr[i][k];
                }
            }
        }
        return max;
    }

    /**
     * Return an array where each element is the sum of the
     * corresponding row of the 2d array
     */
    public static int[] allRowSums(int[][] arr) {
        int[] arr_new = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            for (int k = 0; k < arr[i].length; k++) {
                arr_new[i] += arr[i][k];
            }
        }
        return arr_new;
    }
}

