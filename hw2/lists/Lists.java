package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

import image.In;

/** List problem.
 *  @author Tyler Le
 */
class Lists {


    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        if (L == null) {
            return null;
        }
        IntListList result = new IntListList();
        IntListList resultPointer = result;
        result.head = L;
        while (L.tail != null) {
            if (L.head >= L.tail.head) {
                //IntList copy = L;
                //L = L.tail;
                //copy.tail = null;
                //result = result.tail = new IntListList(L, null);
                result.tail = new IntListList(L.tail, null);
                result = result.tail;
                L.tail = null;
                L = result.head;
            } else {
                L = L.tail;
            }
        }
        return resultPointer;
    }

    /** Same as above, but a recursive version.
     *
     *  If you choose to go with the recursive skeleton, make sure to change the
     *  name from naturalRunsRecursive to naturalRuns, and delete the iterative
     *  skeleton. Otherwise, our autograder will grade the iterative version above.
     * */
    static IntListList naturalRunsRecursive(IntList L) {
        if (L == null) {
            return null;
        }
        IntListList pointer = new IntListList(L, null);
        IntList pointer2 = L;
        while (L != endOfRun(L)) {
            L = L.tail;
        }
        IntListList rest = naturalRuns(L.tail);
        return new IntListList(pointer.head, rest);
    }

    /** Recursive helper method, if you'd like.
     *
     *  Assuming L is not null, returns the last element of L in which
     *  the value of L.head increases from the previous element (the
     *  end of the list if L is entirely in strictly ascending order).  */
    private static IntList endOfRun(IntList L) {
        while (L.tail != null && L.tail.head > L.head) {
            L = L.tail;
        }
        return L;
    }
}