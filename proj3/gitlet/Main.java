package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Tyler Le
 */
public class Main {

    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** Main metadata folder. */
    static final File GITLET_FOLDER = new File(".gitlet");

    /** Folder containing head pointer. */
    static final File HEAD_FOLDER = new File(".gitlet/head");

    /** Folder containing master pointer. */
    static final File MASTER_FOLDER = new File(".gitlet/master");

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }
        File initFile = new File(".gitlet");
        if (!initFile.exists() && args[0] != "init") {
            exitWithError("Not in an initialized Gitlet directory.");
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
    public static void init(String... args) {
        validateNumArgs(args, 1);
        GITLET_FOLDER.mkdir();
        HEAD_FOLDER.mkdir();
        MASTER_FOLDER.mkdir();
        Commit.COMMIT_FOLDER.mkdir();
        Add.INDEX.mkdir();

        Commit initial = new Commit("initial commit", null);
        initial.commit();
        File head = Utils.join(HEAD_FOLDER, initial.getSha());
        Utils.writeObject(head, initial);
        File mast = Utils.join(MASTER_FOLDER, initial.getSha());
        Utils.writeObject(mast, initial);
    }

    public static void add(String... args) {
        validateNumArgs(args, 2);
        Add a = new Add();
        File file = new File(args[1]);
        Blob b = new Blob(file);
        a.add(args[1], b.getName());
        Utils.writeObject(Utils.join(".gitlet/index"), a);
    }

    public static void commit(String... args) {
        validateNumArgs(args, 2);
        Commit parent = Utils.readObject(HEAD_FOLDER, Commit.class);
        Commit curr = new Commit(args[1], "parent");
        // read parent hascode string back into object
        curr.commit();

        //init will make new commit tree with master and head pointer

        //following commits will clone previous (head) commit, add/overwrite staged files in commit node and clear staging area.
        // head and master pointer point to new commit. master always points to tip of the master branch (recent commit). head pointer always points to current branch.

    }

    public static void checkout(String... args) {
        //move head pointer to specified commit, and overwrite working directory with commit
        validateNumArgs(args, 2);

    }

    public static void log(String... args) {
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
