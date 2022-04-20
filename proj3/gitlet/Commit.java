package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Date;
import java.time.Instant;

public class Commit implements Serializable {

    /** Commit message. */
    private String _message;

    /** Time of commit. */
    private LocalDateTime _time;

    /** Previous commit node. */
    private String _parent;

    /** Hashmap of blobs. */
    private HashMap<String, String> _blobs = new HashMap<>();

    /** SHA ID of commit. */
    private String _sha;

    /** Folder of commits. */
    static final File COMMIT_FOLDER = new File(".gitlet/commit");

    public Commit(String message, String parent) {
        this._message = message;
        this._parent = parent;
//        if (this._parent == null) {
//            this._time = LocalDateTime.ofEpochSecond(0, 0, null);
//        } else {
//            Date date = new Date();
//            this._time = LocalDateTime.now();
//        }
    }

    public static Commit fromFile(String message) {
        File file = Utils.join(COMMIT_FOLDER, message);
        return Utils.readObject(file, Commit.class);
    }

    public void commit() {
        File file = Utils.join(COMMIT_FOLDER, this._message);
        Utils.writeObject(file, this);
    }

    public String getMessage() {
        return this._message;
    }

    public LocalDateTime getTimestamp() {
        return this._time;
    }

    public String getParent() {
        return this._parent;
    }

    public String getSha() {
        return this._sha;
    }
}
