package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Tyler Le
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _name = name;
        _perm = perm;
        _notches = notches;
    }


    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (char i : notches().toCharArray()) {
            if (_perm.wrap(alphabet().toInt(i)) == setting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    @Override
    String notches() {
        return _notches;
    }

    /** The name of the rotor. */
    private String _name;
    /** The permutation given by rotor. */
    private Permutation _perm;
    /** The notches corresponding to the rotor. */
    private String _notches;

}
