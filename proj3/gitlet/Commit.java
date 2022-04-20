package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Date;

public class Commit implements Serializable {

    /** Commit message. */
    private String _message;

    /** Time of commit. */
    private Date _time;

    /** Previous commit node. */
    private String _parent;

    /** Hashmap of blobs. */
    private HashMap<String, String> _blobs = new HashMap<>();

    /** SHA ID of commit. */
    private String _sha;

    /** Folder of commits. */
    static final File COMMIT_FOLDER = new File(".gitlet/commit");

    public Commit(String message, String parent) {
        _message = message;
        _parent = parent;
        date();
        _sha = Utils.sha1(_time.toString(), _blobs.toString());
    }

    private void date() {
        if (_parent == null) {
            _time = new Date(0);
        } else {
            _time = new Date(System.currentTimeMillis());
        }
    }
    public static Commit fromFile(String message) {
        File file = Utils.join(COMMIT_FOLDER, message);
        return Utils.readObject(file, Commit.class);
    }

    public void commit() {
        File file = Utils.join(COMMIT_FOLDER, _sha);
        //_blobs.put();
        Utils.writeObject(file, this);
    }

    public String getMessage() {
        return _message;
    }

    public Date getTime() {
        return _time;
    }

    public String getParent() {
        return _parent;
    }

    public String getSha() {
        return _sha;
    }
}
