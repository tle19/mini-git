package gitlet;

import java.io.File;
import java.util.Arrays;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Tyler Le
 */
public class Main {

    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

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

    /** Text file containing the current branch. */
    private static File _current = new File(".gitlet/branch.txt");

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }
        if (!GITLET_FOLDER.exists() && !args[0].equals("init")) {
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
        case "rm":
            rm(args);
            break;
        case "log":
            log(args);
            break;
        case "global-log":
            globalLog(args);
            break;
        case "find":
            find(args);
            break;
        case "status":
            status(args);
            break;
        case "checkout":
            checkout(args);
            break;
        case "branch":
            branch(args);
            break;
        case "rm-branch":
            rmBranch(args);
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
        File initFile = new File(".gitlet");
        if (initFile.exists()) {
            exitWithError("A Gitlet version-control system "
                    + "already exists in the current directory.");
        }

        GITLET_FOLDER.mkdir();
        BLOBS.mkdir();
        INDEX.mkdir();
        COMMIT_FOLDER.mkdir();
        REFS.mkdir();
        HEAD.mkdir();

        File stage = Utils.join(INDEX, "stage");
        Utils.writeObject(stage, new Storage());

        File remove = Utils.join(INDEX, "remove");
        Utils.writeObject(remove, new Storage());

        File blobs = Utils.join(BLOBS, "blobs");
        Utils.writeObject(stage, new Storage());

        Commit initial = new Commit("initial commit", null, null);
        initial.commit();

        File mast = Utils.join(REFS, "master");
        Utils.writeObject(mast, initial);

        File head = Utils.join(HEAD, initial.getSha());
        Utils.writeObject(head, initial);

        Utils.writeContents(_current, "master");
    }

    public static void add(String... args) {
        validateNumArgs(args, 2);

        Commit curr = Utils.readObject(HEAD.listFiles()[0], Commit.class);
        File exist = new File(args[1] + "");
        if (!exist.exists() && !curr.getBlob().containsKey(args[1])) {
            exitWithError("File does not exist.");
        }

        File remove = Utils.join(INDEX, "remove");
        Storage removed = Utils.readObject(remove, Storage.class);
        if (removed.contains(args[1])) {
            Utils.writeContents(Utils.join(args[1]),
                    removed.getBlob().get(args[1]).getBlob());
            removed.remove(args[1]);
            Utils.writeObject(remove, removed);
            System.exit(0);
        }

        File file = new File(args[1]);
        Blob b = new Blob(file, args[1]);

        if (curr.getBlob().containsKey(args[1])) {
            if (curr.getBlob().get(args[1]).equals(b.getHash())) {
                System.exit(0);
            }
            if (b.getHash().equals(curr.getBlob().get(args[1]))) {
                Utils.writeContents(Utils.join(args[1]),
                        curr.getBlob().get(args[1]).getBytes());
                removed.remove(args[1]);
                Utils.writeObject(remove, removed);
                System.exit(0);

            }
        }

        File blobLocation = Utils.join(BLOBS, b.getHash());
        Utils.writeObject(blobLocation, b);

        File stage = Utils.join(INDEX, "stage");
        Storage staged = Utils.readObject(stage, Storage.class);
        staged.put(args[1], b);
        Utils.writeObject(stage, staged);
    }

    public static void commit(String... args) {
        validateNumArgs(args, 2);
        File stage = Utils.join(INDEX, "stage");
        Storage staged = Utils.readObject(stage, Storage.class);
        File remove = Utils.join(INDEX, "remove");
        Storage removed = Utils.readObject(remove, Storage.class);
        if (staged.getBlob().keySet().size() == 0
                && removed.getBlob().keySet().size() == 0) {
            exitWithError("No changes added to the commit.");
        }
        if (args[1].equals("")) {
            exitWithError("Please enter a commit messasge.");
        }

        Commit parent = Utils.readObject(HEAD.listFiles()[0], Commit.class);
        Commit curr = new Commit(args[1], parent.getSha(), parent);
        curr.putAll(parent.getBlob());

        for (String keys : staged.getBlob().keySet()) {
            curr.put(keys, staged.get(keys).getHash());
        }
        for (String keys : removed.getBlob().keySet()) {
            curr.getBlob().remove(keys);
        }

        staged.clear();
        removed.clear();
        Utils.writeObject(stage, staged);
        Utils.writeObject(remove, removed);

        String path = "master";
        for (File file : REFS.listFiles()) {
            if (Utils.readObject(file, Commit.class).getSha().equals
                    (HEAD.listFiles()[0].getName())) {
                path = file.getName();
                break;
            }
        }
        curr.commit();
        HEAD.listFiles()[0].delete();
        File head = Utils.join(REFS, path);
        Utils.writeObject(head, curr);
        File f = Utils.join(HEAD, curr.getSha());
        Utils.writeObject(f, curr);
    }

    public static void rm(String... args) {
        validateNumArgs(args, 2);
        File stage = Utils.join(INDEX, "stage");
        Storage staged = Utils.readObject(stage, Storage.class);
        File remove = Utils.join(INDEX, "remove");
        Storage removed = Utils.readObject(remove, Storage.class);
        Commit curr = Utils.readObject(HEAD.listFiles()[0], Commit.class);

        if (staged.contains(args[1])) {
            staged.remove(args[1]);
            Utils.writeObject(stage, staged);
        } else if (curr.getBlob().containsKey(args[1])) {
            String s = curr.getBlob().get(args[1]);
            removed.put(args[1],
                    Utils.readObject(Utils.join(BLOBS, s), Blob.class));
            Utils.join(args[1]).delete();
            Utils.writeObject(remove, removed);
        } else {
            exitWithError("No reason to remove the file.");
        }

    }

    public static void log(String... args) {
        validateNumArgs(args, 1);
        Commit curr = Utils.readObject(HEAD.listFiles()[0], Commit.class);
        while (curr != null) {
            curr.log();
            curr = curr.getParent2();
        }
    }

    public static void globalLog(String... args) {
        validateNumArgs(args, 1);
        for (File f : COMMIT_FOLDER.listFiles()) {
            Utils.readObject(f, Commit.class).log();
        }
    }

    public static void find(String... args) {
        validateNumArgs(args, 2);
        boolean exists = false;
        for (File file : COMMIT_FOLDER.listFiles()) {
            Commit curr = Utils.readObject(file, Commit.class);
            if (curr.getMessage().equals(args[1])) {
                System.out.println(curr.getSha());
                exists = true;
            }
        }
        if (!exists) {
            exitWithError("Found no commit with that message.");
        }
    }

    public static void status(String... args) {
        System.out.println("=== Branches ===");
        for (File file : REFS.listFiles()) {
            if (Arrays.equals(Utils.readContents(_current),
                    file.getName().getBytes())) {
                System.out.println("*" + file.getName());
            } else {
                System.out.println(file.getName());
            }
        }
        System.out.println('\n' + "=== Staged Files ===");
        File stage = Utils.join(INDEX, "stage");
        Storage staged = Utils.readObject(stage, Storage.class);
        for (String keys : staged.getBlob().keySet()) {
            System.out.println(keys);
        }
        System.out.println('\n' + "=== Removed Files ===");
        File remove = Utils.join(INDEX, "remove");
        Storage removed = Utils.readObject(remove, Storage.class);
        for (String keys : removed.getBlob().keySet()) {
            System.out.println(keys);
        }
        System.out.println('\n'
                + "=== Modifications Not Staged For Commit ===");
        System.out.println('\n' + "=== Untracked Files ===");
        Commit curr = Utils.readObject(HEAD.listFiles()[0], Commit.class);
//        for (String s : Utils.plainFilenamesIn(CWD)) {
//            if (!curr.getBlob().containsKey(s)
//                    && !curr.getBlob().get(s).equals(Utils.sha1(
//                            Utils.readContentsAsString(Utils.join(s))))) {
//                System.out.println(s);
//            }
//        }
        System.out.println('\n');

    }

    public static void checkout(String... args) {
        if (args.length == 3 && args[1].equals("--")) {
            File file = new File(args[2] + "");
            if (!file.exists()) {
                System.out.println("File does not exist in that commit.");
            }
            Commit head = Utils.readObject(HEAD.listFiles()[0], Commit.class);
            String blob = head.getBlob().get(args[2]);
            if (blob != null) {
                Blob cont = Utils.readObject(Utils.join(BLOBS, blob),
                        Blob.class);
                Utils.writeContents(Utils.join(args[2]), cont.getBlob());
            }
        } else if (args.length == 4 && args[2].equals("--")) {
            boolean commitTrue = false;
            String trueID = null;
            for (File f : COMMIT_FOLDER.listFiles()) {
                String shortID = f.getName().substring(0, 8);
                if (f.getName().equals(args[1]) || shortID.equals(args[1])) {
                    commitTrue = true;
                    trueID = f.getName();
                    break;
                }
            }
            if (!commitTrue) {
                exitWithError("No commit with that id exists.");
            }
            File file = new File(args[3] + "");
            if (!file.exists()) {
                exitWithError("File does not exist in that commit.");
            }
            Commit curr = Utils.readObject(Utils.join(COMMIT_FOLDER, trueID),
                    Commit.class);
            String blob = curr.getBlob().get(args[3]);
            if (blob != null) {
                Blob cont = Utils.readObject(Utils.join(BLOBS, blob),
                        Blob.class);
                Utils.writeContents(Utils.join(args[3]), cont.getBlob());
            }
        } else if (args.length == 2) {
            checkout2(args[1]);
        } else {
            exitWithError("Incorrect operands.");
        }
    }

    public static void checkout2(String args) {
        boolean commitTrue = false;
        for (File file : REFS.listFiles()) {
            if (file.getName().equals(args)) {
                commitTrue = true;
                break;
            }
        }
        if (!commitTrue) {
            exitWithError("No such branch exists.");
        }

        Commit curr = Utils.readObject(HEAD.listFiles()[0], Commit.class);
        Commit br = Utils.readObject(Utils.join(REFS, args), Commit.class);

        for (String s : Utils.plainFilenamesIn(CWD)) {
            if (!curr.getBlob().containsKey(s)
                    && !br.getBlob().get(s).equals(Utils.sha1(
                            Utils.readContentsAsString(Utils.join(s))))) {
                exitWithError("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
            }
        }
        if (Arrays.equals(Utils.readContents(_current), args.getBytes())) {
            exitWithError("No need to checkout the current branch.");
        }

        for (String keys : br.getBlob().keySet()) {
            String blob = br.getBlob().get(keys);
            if (blob != null) {
                Blob cont = Utils.readObject(Utils.join(BLOBS, blob),
                        Blob.class);
                Utils.writeContents(Utils.join(keys), cont.getBlob());
            }
        }
        for (String keys : curr.getBlob().keySet()) {
            if (!br.getBlob().containsKey(keys)) {
                Utils.join(keys).delete();
            }
        }
        Utils.writeContents(_current, args);
        File head = Utils.join(HEAD, br.getSha());
        HEAD.listFiles()[0].delete();
        Utils.writeObject(head, br);
    }

    public static void branch(String... args) {
        validateNumArgs(args, 2);
        for (File file : REFS.listFiles()) {
            if (file.getName().equals(args[1])) {
                exitWithError("A branch with that name already exists.");
            }
        }
        File newBranch = Utils.join(REFS, args[1]);
        File head = HEAD.listFiles()[0];
        Commit header = Utils.readObject(head, Commit.class);
        Utils.writeObject(newBranch, header);
    }

    public static void rmBranch(String... args) {
        validateNumArgs(args, 2);
        boolean exists = false;
        for (File file : REFS.listFiles()) {
            if (file.getName().equals(args[1])) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            exitWithError("A branch with that name does not exist.");
        }
        Commit curr = Utils.readObject(HEAD.listFiles()[0], Commit.class);
        if (Arrays.equals(Utils.readContents(_current), args[1].getBytes())) {
            exitWithError("Cannot remove the current branch.");
        }

        Utils.join(REFS, args[1]).delete();
        Utils.writeContents(_current, "");
    }

    public static void reset(String... args) {
        validateNumArgs(args, 2);
        boolean commitTrue = false;
        for (File f : COMMIT_FOLDER.listFiles()) {
            String shortID = f.getName().substring(0, 8);
            if (f.getName().equals(args[1]) || shortID.equals(args[1])) {
                commitTrue = true;
                break;
            }
        }
        if (!commitTrue) {
            exitWithError("No commit with that id exists.");
        }

        Commit curr = Utils.readObject(Utils.join(COMMIT_FOLDER, args[1]),
                Commit.class);

//        for (String s : Utils.plainFilenamesIn(CWD)) {
//            if (!curr.getBlob().containsKey(s) &&
//                    !curr.getBlob().get(s).equals(Utils.sha1(
//                            Utils.readContentsAsString(Utils.join(s))))) {
//                exitWithError("There is an untracked file in the way; "
//                        + "delete it, or add and commit it first.");
//            }
//        }

        for (String keys : curr.getBlob().keySet()) {
            String blob = curr.getBlob().get(keys);
            if (blob != null) {
                Blob cont = Utils.readObject(Utils.join(BLOBS, blob),
                        Blob.class);
                Utils.writeContents(Utils.join(keys), cont.getBlob());
            }
        }
        File stage = Utils.join(INDEX, "stage");
        Storage staged = Utils.readObject(stage, Storage.class);
        staged.clear();
        Utils.writeObject(stage, staged);

        File remove = Utils.join(INDEX, "remove");
        Storage removed = Utils.readObject(remove, Storage.class);
        removed.clear();
        Utils.writeObject(remove, removed);

        File head = Utils.join(HEAD, curr.getSha());
        Utils.writeObject(head, curr);
    }

    public static void merge(String... args) {
        validateNumArgs(args, 2);
        
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
     * @param args Argument array from command line
     * @param n Number of arguments being validated
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            exitWithError("Incorrect operands.");
        }
    }

}
