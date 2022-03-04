package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Tyler Le
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _advanced = false;
        _rings = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Return my current ring setting. */
    int rings() {
        return _rings;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = permutation().alphabet().toInt(cposn);
    }

    /** Set _rings to character RING. */
    void setRing(int ring) {
        if (ring == 0) {
            _rings = 0;
        } else {
            _rings = ring;
        }
    }

    /** Set _rings to character CRING. */
    void setRing(char cring) {
        String comp = "" + cring;
        if (comp.isEmpty()) {
            _rings = 0;
        } else {
            _rings = permutation().alphabet().toInt(cring);
        }
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int permuted = permutation().wrap(p + setting()) - _rings;
        int first = permutation().permute(permuted);
        return permutation().wrap(first - setting() + _rings);
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int inverted = permutation().wrap(e + setting()) - _rings;
        int first = permutation().invert(inverted);
        return permutation().wrap(first - setting() + _rings);
    }

    /** Returns the positions of the notches, as a string giving the letters
     *  on the ring at which they occur. */
    String notches() {
        return "";
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;
    /** The current position of the notch on the rotor. */
    private int _setting;
    /** Whether or not the rotor has advanced this turn. */
    protected boolean _advanced;
    /** The current position of the rings on the rotor. */
    private int _rings;

}
