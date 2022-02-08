package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author Tyler Le
 */

public class ListsTest {

    @Test
    public void basicRunsTest() {
        IntList input = IntList.list(1, 2, 3, 1, 2);
        IntList run1 = IntList.list(1, 2, 3);
        IntList run2 = IntList.list(1, 2);
        IntListList result = IntListList.list(run1, run2);
        assertEquals(result, Lists.naturalRuns(input));

        IntList input2 = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        IntList run3 = IntList.list(1, 3, 7);
        IntList run4 = IntList.list(5);
        IntList run5 = IntList.list(4, 6, 9, 10);
        IntList run6 = IntList.list(10, 11);
        IntListList result2 = IntListList.list(run3, run4, run5, run6);
        assertEquals(result2, Lists.naturalRuns(input2));
    }

    @Test
    public void advancedRunsTest() {
        assertEquals(null, Lists.naturalRuns(null));

        IntList input = IntList.list(3);
        IntListList result = IntListList.list(input);
        assertEquals(result, Lists.naturalRuns(input));

        IntList input2 = IntList.list(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
        IntList run0 = IntList.list(9);
        IntList run1 = IntList.list(8);
        IntList run2 = IntList.list(7);
        IntList run3 = IntList.list(6);
        IntList run4 = IntList.list(5);
        IntList run5 = IntList.list(4);
        IntList run6 = IntList.list(3);
        IntList run7 = IntList.list(2);
        IntList run8 = IntList.list(1);
        IntList run9 = IntList.list(0);
        IntListList result2 = IntListList.list(run0, run1, run2, run3, run4, run5, run6, run7, run8, run9);
        assertEquals(result2, Lists.naturalRuns(input2));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
