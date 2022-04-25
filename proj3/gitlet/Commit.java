package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;

public class Commit implements Serializable {

    /** Commit message. */
    private String _message;

    /** Time of commit. */
    private Date _time;

    /** Previous commit node as a sha1 value. */
    private String _parent;

    /** Previous commit node. */
    private Commit _parent2;

    /** Hashmap of blobs. */
    private HashMap<String, String> _blobs = new HashMap<>();

    /** SHA ID of commit. */
    private String _sha;

    public Commit(String message, String parent, Commit parent2) {
        _message = message;
        _parent = parent;
        _parent2 = parent2;
        if (_parent == null) {
            _time = new Date(0);
        } else {
            _time = new Date(System.currentTimeMillis());
        }
    }

    public void commit() {
        _sha = Utils.sha1(_time.toString(), _blobs.toString());

        File file = Utils.join(Main.COMMIT_FOLDER, _sha);
        Utils.writeObject(file, this);

        File head = Utils.join(Main.HEAD, getSha());
        Utils.writeObject(head, this);

        File mast = Utils.join(Main.MASTER, getSha());
        Utils.writeObject(mast, this);
    }

    public void put(String key, String val) {
        _blobs.put(key, val);
    }

    public void putAll(HashMap<String, String> map) {
        _blobs.putAll(map);
    }

    public void log() {
        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        System.out.println("===");
        System.out.println("commit " + getSha());
        String date = simpleDateFormat.format(getTime());
        System.out.println("Date: " + date);
        System.out.println(getMessage() + '\n');
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

    public Commit getParent2() {
        return _parent2;
    }

    public String getSha() {
        return _sha;
    }

    public HashMap<String, String> getBlob() {
        return _blobs;
    }


}
