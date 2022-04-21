package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Tyler Le
 */
public class Main {

    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** Main metadata folder. */
    static final File GITLET_FOLDER = new File(".gitlet");

    /** Folder of blobs. */
    static final File BLOBS = new File(".gitlet/blobs");

    /** Folder of files that are staged. */
    static final File INDEX = new File(".gitlet/index");

    /** Folder of commits. */
    static final File COMMIT_FOLDER = new File(".gitlet/commit");

    /** Folder of branches. */
    static final File REFS = new File(".gitlet/refs");

    /** Folder containing head pointer. */
    static final File HEAD = new File(".gitlet/head");

    /** Folder containing master pointer. */
    static final File MASTER = new File(".gitlet/master");


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
            init(args); //init()
            break;
        case "add":
            add(args);
            break;
        case "commit":
            commit(args);
            break;
        case "rm":
            rm(args);
            break;
        case "log":
            log(args); //log()
            break;
        case "global-log":
            global_log(args); //log()
            break;
        case "find":
            find(args);
            break;
        case "status":
            status(args); //status()
            break;
        case "checkout":
            checkout(args);
            break;
        case "branch":
            branch(args);
            break;
        case "rm-branch":
            rm_branch(args);
            break;
        case "reset":
            reset(args);
            break;
        case "merge":
            merge(args);
            break;
        default:
            exitWithError("No command with that name exists.");
        }
    }

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * @param args Command argument. */
    public static void init(String... args) {
        validateNumArgs(args, 1);

        GITLET_FOLDER.mkdir();
        BLOBS.mkdir();
        INDEX.mkdir();
        COMMIT_FOLDER.mkdir();
        REFS.mkdir();
        HEAD.mkdir();
        MASTER.mkdir();

        Commit initial = new Commit("initial commit", null, null);
        initial.commit();
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

        File blobLocation = Utils.join(BLOBS, b.getHash());
        Utils.writeObject(blobLocation, b);
        File stage = Utils.join(INDEX, b.getHash());
        Utils.writeObject(stage, b);
    }

    public static void commit(String... args) {
        validateNumArgs(args, 2);
        Commit parent = Utils.readObject(HEAD.listFiles()[0], Commit.class);
        Commit curr = new Commit(args[1], parent.getSha(), parent);
        curr.replace(parent.getBlob());

        for (File file : INDEX.listFiles()) {
            Blob b = Utils.readObject(file, Blob.class);
            curr.put(b.getFileName(), b.getHash());
            file.delete();
        }
        HEAD.listFiles()[0].delete();
        MASTER.listFiles()[0].delete();
        curr.commit();
    }

    public static void rm(String... args) {

    }

    public static void log(String... args) {
        validateNumArgs(args, 1);
        Commit curr = Utils.readObject(HEAD.listFiles()[0], Commit.class);
        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        while (curr != null) {
            System.out.println("===");
            System.out.println("commit " + curr.getSha());
            String date = simpleDateFormat.format(curr.getTime());
            System.out.println("Date: " + date);
            System.out.println(curr.getMessage() + '\n');
            curr = curr.getParent2();
        }
    }

    public static void global_log(String... args) {

    }

    public static void find(String... args) {

    }

    public static void status(String... args) {

    }

    public static void checkout(String... args) {
        if (args.length == 3) { //&& args[1] == "--"
            Commit head = Utils.readObject(HEAD.listFiles()[0], Commit.class);
            String blob = head.getBlob().get(args[2]);
            if (blob != null) {
                Blob cont = Utils.readObject(Utils.join(BLOBS, blob), Blob.class);
                Utils.writeContents(Utils.join(args[2]), cont.getBlob());
            }
        } else if (args.length == 4) { //&& args[2] == "--"
            Commit curr = Utils.readObject(Utils.join(COMMIT_FOLDER, args[1]), Commit.class);
            String blob = curr.getBlob().get(args[3]);
            if (blob != null) {
                Blob cont = Utils.readObject(Utils.join(BLOBS, blob), Blob.class);
                Utils.writeContents(Utils.join(args[3]), cont.getBlob());
            }
        } else {
            exitWithError("Incorrect operands.");
        }
    }

    public static void branch(String... args) {

    }

    public static void rm_branch(String... args) {

    }

    public static void reset(String... args) {

    }

    public static void merge(String... args) {

    }

    /**
     * Prints out MESSAGE and exits with error code 0.
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
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            exitWithError("Incorrect operands.");
        }
    }

}
