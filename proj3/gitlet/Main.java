package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Tyler Le
 */
public class Main {

    /** Current Working Directory. */
    static final File CWD = new File(".");
    // static final File CWD = new File(System.getProperty("user.dir"))

    /** Main metadata folder. */
    static final File GITLET_FOLDER = new File(".gitlet");

    /** Folder of commits. */
    static final File COMMIT_FOLDER = new File(".gitlet/commit");

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String[] args) {
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }
        switch (args[0]) {
            case "init":
                init(args);
                break;
            case "add":
                add(args);
                break;
            case "commit":
                commit(args);
                break;
            case "checkout":
                checkout(args);
                break;
            case "log":
                log(args);
                break;
            default:
                exitWithError("No command with that name exists.");
        }
        return;
    }

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     */
    public static void init(String[] args) {
        validateNumArgs(args, 1);
        //make necessary files and make first commit
        GITLET_FOLDER.mkdir();
        COMMIT_FOLDER.mkdir();
        Add.INDEX.mkdir();
        //commit();
    }

    public static void add(String[] args) {
        validateNumArgs(args, 2);
        //put file in staging area
    }

    private static void commit() {
        String[] msg = {"initial commit"};
        commit(msg);
        Commit initial = new Commit("initial commit", null);
    }

    public static void commit(String[] args) {
        validateNumArgs(args, 2);
        //init will make new commit tree with master and head pointer

        //following commits will clone previous (head) commit, add/overwrite staged files in commit node and clear staging area.
        // head and master pointer point to new commit. master always points to tip of the master branch (recent commit). head pointer always points to current branch.

    }

    public static void checkout(String[] args) {
        //move head pointer to specified commit, and overwrite working directory with commit
        validateNumArgs(args, 2);

    }

    public static void log(String[] args) {
        validateNumArgs(args, 1);
    }

    /**
     * Prints out MESSAGE and exits with error code -1.
     * @param message message to print
     */
    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(0);
    }

    /**
     * Checks the number of arguments versus the expected number,
     * exits if it does not.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            exitWithError("Incorrect operands.");
        }
    }

}
