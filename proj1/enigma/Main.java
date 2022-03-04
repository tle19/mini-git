package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Tyler Le
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine configured = readConfig();
        setUp(configured, _input.nextLine());
        while (_input.hasNextLine()) {
            String next = _input.nextLine();
            if (next.contains("*")) {
                setUp(configured, next);
            } else {
                printMessageLine(configured.convert(next.replaceAll(" ", "")));
            }
        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            List<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String gtype = _config.next();
            String cycles = "";
            while (_config.hasNext("\\(.*\\)")) {
                cycles += _config.next();
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            char type = gtype.charAt(0);
            if (type == 'M') {
                String notches = "";
                for (int i = 1; i < gtype.length(); i++) {
                    notches += gtype.charAt(i);
                }
                return new MovingRotor(name, perm, notches);
            } else if (type == 'N') {
                return new FixedRotor(name, perm);
            } else if (type == 'R') {
                return new Reflector(name, perm);
            } else {
                throw error("Incorrect rotor format");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        try {
            String[] settingsArr = settings.split(" ");
            String[] rotors = new String[M.numRotors()];
            String sett = "";
            String cycles = "";
            String ring = "";
            Permutation perm;
            if (settingsArr[0].equals("*")) {
                for (int i = 0; i < rotors.length; i++) {
                    rotors[i] = settingsArr[i + 1];
                }
                sett = settingsArr[rotors.length + 1];
                for (int i = rotors.length + 2; i < settingsArr.length; i++) {
                    if (settingsArr[i].charAt(0) == '(') {
                        cycles += settingsArr[i];
                    } else {
                        ring += settingsArr[i];
                    }
                }
                perm = new Permutation(cycles, _alphabet);
            } else {
                throw new EnigmaException("incorrect formatting of file.in");
            }
            M.setPlugboard(perm);
            M.insertRotors(rotors);
            M.setRings(ring);
            M.setRotors(sett);
            int moving = 0;
            for (int i = 0; i < M.numRotors(); i++) {
                if (M.getRotor(i) instanceof MovingRotor) {
                    moving++;
                }
            }
            if (M.numPawls() != moving) {
                throw new EnigmaException("rotors do not match arguments");
            }
        } catch (ArrayIndexOutOfBoundsException excp) {
            throw error("invalid amount of rotors");
        }
    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String result = "";
        for (int i = 0, k = 5; i < msg.length(); i += 5, k += 5) {
            k = Math.min(k, msg.length());
            result += msg.substring(i, k) + " ";
        }
        _output.println(result);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;

}
