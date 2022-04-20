package gitlet;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;

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

    /** Folder of blobs. */
    static final File BLOBS = new File(".gitlet/blobs");

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
        BLOBS.mkdir();

        Commit initial = new Commit("initial commit", null, null);
        initial.commit();
        File head = Utils.join(HEAD_FOLDER, initial.getSha());
        Utils.writeObject(head, initial);
        File mast = Utils.join(MASTER_FOLDER, initial.getSha());
        Utils.writeObject(mast, initial);
    }

    public static void add(String... args) {
        validateNumArgs(args, 2);
        File file = new File(args[1]);
        Blob b = new Blob(file, args[1]);

        for (File f : BLOBS.listFiles()) {
            Blob blub = Utils.readObject(f, Blob.class);
            if (blub.getFileName() == args[1]) {
                f.delete();
            }
        }

        File blobLocation = Utils.join(BLOBS, b.getName());
        Utils.writeObject(blobLocation, b);
        File stage = Utils.join(INDEX, b.getName());
        Utils.writeObject(stage, b);
    }

    public static void commit(String... args) {
        validateNumArgs(args, 2);
        Commit parent = Utils.readObject(HEAD_FOLDER.listFiles()[0], Commit.class);
        Commit curr = new Commit(args[1], parent.getSha(), parent);
        for (String key : parent.getBlob().keySet()) {
            curr.commitAdd(key, parent.getBlob().get(key));

        }
        for (File f : INDEX.listFiles()) {
            Blob b = Utils.readObject(f, Blob.class);
            curr.commitAdd(b.getFileName(), b.getName());
        }


        curr.commit();
        File file0 = new File(".gitlet/index");
        File file1 = new File(".gitlet/head");
        File file2 = new File(".gitlet/master");
        file0.listFiles()[0].delete();
        file1.listFiles()[0].delete();
        file2.listFiles()[0].delete();
        File head = Utils.join(HEAD_FOLDER, curr.getSha());
        Utils.writeObject(head, curr);
        File mast = Utils.join(MASTER_FOLDER, curr.getSha());
        Utils.writeObject(mast, curr);
    }

    public static void checkout(String... args) {
        //move head pointer to specified commit, and overwrite working directory with commit
        if (args.length == 3) { //&& args[1] == "--"
            Commit head = Utils.readObject(HEAD_FOLDER.listFiles()[0], Commit.class);
            String blob = head.getBlob().get(args[2]);
            if (blob != null) {
                Blob cont = Utils.readObject(Utils.join(BLOBS, blob), Blob.class);
                Utils.writeContents(Utils.join(args[2]), cont.getBlob());
            }
        } else if (args.length == 4) { //&& args[2] == "--"
            Commit curr = Utils.readObject(Utils.join(".gitlet/commit", args[1]), Commit.class);
            String blob = curr.getBlob().get(args[3]);
            if (blob != null) {
                Blob cont = Utils.readObject(Utils.join(BLOBS, blob), Blob.class);
                Utils.writeContents(Utils.join(args[3]), cont.getBlob());
            }
        } else {
            exitWithError("Incorrect operands.");
        }
    }

    public static void log(String... args) {
        validateNumArgs(args, 1);
        Commit curr = Utils.readObject(HEAD_FOLDER.listFiles()[0], Commit.class);
        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern, new Locale("en", "US"));
        while (curr != null) {
            System.out.println("===");
            System.out.println("commit " + curr.getSha());
            String date = simpleDateFormat.format(curr.getTime());
            System.out.println("Date: " + date);
            System.out.println(curr.getMessage() + '\n');
            curr = curr.getParent2();
        }
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
