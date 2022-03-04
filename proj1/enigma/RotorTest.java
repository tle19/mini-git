package enigma;

import org.junit.Test;

import static enigma.TestUtils.*;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Rotor class.
 *  @author Tyler Le
 */

public class RotorTest {

    @Test
    public void setRingsTest() {
        Alphabet alpha = new Alphabet();
        Permutation perm = new Permutation("(BAC) (FED) (HLIJKZG)", alpha);
        MovingRotor m = new MovingRotor("I", perm, "G");
        String test = "BCD";
        m.setRing(test.charAt(0));
        assertEquals(1, m.rings());
        m.setRing(0);
        assertEquals(0, m.rings());
    }

    @Test
    public void ringsPermuteTest() {
        Alphabet alpha = new Alphabet();
        Permutation perm = new Permutation("(BAC) (FED) (HLIJKZG)", alpha);
        MovingRotor m = new MovingRotor("I", perm, "G");
        String test = "BCD";
        m.setRing(test.charAt(0));
        assertEquals(1, m.rings());
        int result = 0;
        for (int i = 0; i < 5; i++) {
            result = m.convertForward(result);
        }
        assertEquals(10, result);
        result = 0;
        for (int i = 0; i < 5; i++) {
            result = m.convertBackward(result);
        }
        assertEquals(8, result);
    }
}
