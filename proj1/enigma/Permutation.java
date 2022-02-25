package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Tyler Le
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles += "(" + cycle + ")";
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char wrapped = _alphabet.toChar(wrap(p));
        if (_alphabet.contains(wrapped)) {
            char result = wrapped;
            int pos = 0;
            for (int i = 0; i < _cycles.length(); i = pos) {
                if (_cycles.charAt(i) == '(') {
                    for (int k = i; _cycles.charAt(k) != ')'; k++) {
                        pos = k;
                        if (wrapped == _cycles.charAt(k)) {
                            if (_cycles.charAt(k + 1) == ')') {
                                result = _cycles.charAt(i + 1);
                            } else {
                                result = _cycles.charAt(k + 1);
                            }
                        }
                    }
                }
                else {
                    pos++;
                }
            }
            return _alphabet.toInt(result);
        }
        throw new EnigmaException("character not in alphabet");
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c)  {
        char wrapped = _alphabet.toChar(wrap(c));
        if (_alphabet.contains(wrapped)) {
            char result = wrapped;
            int pos = 0;
            for (int i = _cycles.length() - 1; i > 0; i = pos) {
                if (_cycles.charAt(i) == ')') {
                    for (int k = i; _cycles.charAt(k) != '('; k--) {
                        pos = k;
                        if (wrapped == _cycles.charAt(k)) {
                            if (_cycles.charAt(k - 1) == '(') {
                                result = _cycles.charAt(i - 1);
                            } else {
                                result = _cycles.charAt(k - 1);
                            }
                        }

                    }
                } else {
                    pos--;
                }
            }
            return _alphabet.toInt(result);
        }
        throw new EnigmaException("character not in alphabet");
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int permutedChar = permute(_alphabet.toInt(p));
        return _alphabet.toChar(permutedChar);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int permutedChar = invert(_alphabet.toInt(c));
        return _alphabet.toChar(permutedChar);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int result = 0;
        for (char ch : _alphabet.alphabet().toCharArray()) {
            for (char k : _cycles.toCharArray()) {
                if (ch == k) {
                    result++;
                }
            }
            result--;
        }
        return result >= 0;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    private String _cycles;

}
