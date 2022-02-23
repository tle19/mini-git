package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Alphabet class.
 *  @author Tyler Le
 */
public class AlphabetTest {

    @Test
    public void testSimple() {
        Alphabet test = new Alphabet("ABCD");
        assertEquals(4, test.size());
        assertTrue(test.contains('A'));
        assertFalse(test.contains('V'));
        assertEquals(0, test.toInt('A'));
        assertEquals(3, test.toInt('D'));
        assertEquals('B', test.toChar(1));
        assertEquals('A', test.toChar(0));

        Alphabet test2 = new Alphabet();
        assertEquals(26, test2.size());
        assertTrue(test2.contains('A'));
        assertTrue(test2.contains('V'));
        assertEquals(4, test2.toInt('E'));
        assertEquals(25, test2.toInt('Z'));
        assertEquals('Y', test2.toChar(24));
        assertEquals('A', test2.toChar(0));
    }

    @Test
    public void testAdvanced() {
        String testString = "abcdefzyxw1092";
        Alphabet test = new Alphabet(testString);
        assertEquals(14, test.size());
        for (int i = 0; i < testString.length(); i++) {
            char curr = testString.charAt(i);
            assertEquals(curr, test.toChar(i));
            assertEquals(i, test.toInt(curr));
            assertTrue(test.contains(curr));
        }
    }

}
