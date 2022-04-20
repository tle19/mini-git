package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    /** Folder of files that are staged */
    static final File INDEX = new File(".gitlet/index");

    /** Folder of commits. */
    static final File COMMIT_FOLDER = new File(".gitlet/commit");

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }
//        File initFile = new File(".gitlet");
//        if (!initFile.exists() && !args[0].equals("init")) {
//            exitWithError("Not in an initialized Gitlet directory.");
//        }
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
        COMMIT_FOLDER.mkdir();
        INDEX.mkdir();

        Commit initial = new Commit("initial commit", null);
        //initial.commitAdd(null);
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
        File stage = new File(".gitlet/index/staged");
        Utils.writeObject(stage, a);
    }

    public static void commit(String... args) {
        validateNumArgs(args, 2);
        Commit parent = Utils.readObject(HEAD_FOLDER.listFiles()[0], Commit.class);
        Commit curr = new Commit(args[1], parent.getSha());
        File file0 = new File(".gitlet/index");
        File file1 = new File(".gitlet/head");
        File file2 = new File(".gitlet/master");
        Add a = Utils.readObject(INDEX.listFiles()[0], Add.class);
        for (String name : (Set<String>) parent.getBlob().keySet()) {
            curr.commitAdd(name, (String) parent.getBlob().get(name));
        }

        curr.commit();
        File[] del0 = file0.listFiles();
        File[] del1 = file1.listFiles();
        File[] del2 = file2.listFiles();
        del0[0].delete();
        del1[0].delete();
        del2[0].delete();
        File head = Utils.join(HEAD_FOLDER, curr.getSha());
        Utils.writeObject(head, curr);
        File mast = Utils.join(MASTER_FOLDER, curr.getSha());
        Utils.writeObject(mast, curr);
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
