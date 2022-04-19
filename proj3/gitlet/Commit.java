package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Commit implements Serializable {

    private String message;
    private String timestamp;
    private String parent;
    private Blob[] blobs;

    /** Folder of commits. */
    static final File COMMIT_FOLDER = new File(".gitlet/commit");

    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        if (this.parent == null) {
            this.timestamp = "00:00:00 UTC, Thursday, 1 January 1970";
        }

    }

    public static Commit fromFile(String message) {
        File file = Utils.join(COMMIT_FOLDER, message);
        return Utils.readObject(file, Commit.class);
    }

    public void commit() {
        File file = Utils.join(COMMIT_FOLDER, this.message);
        Utils.writeObject(file, this);
    }

    public String getMessage() {
        return this.message;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getParent() {
        return this.parent;
    }
}
