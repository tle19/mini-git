package gitlet;

import java.io.Serializable;
import java.io.File;

public class Blob implements Serializable {

    /** The bytes of the file. */
    private byte[] _blob;

    /** The sha1 hash of the file. */
    private String _hash;

    /** The filename of the file. */
    private String _filename;

    Blob(File file, String filename) {
        _filename = filename;
        _blob = Utils.readContents(file);
        _hash = Utils.sha1(_blob);
    }

    public String getHash() {
        return _hash;
    }

    public byte[] getBlob() {
        return _blob;
    }

    public String getFileName() {
        return _filename;
    }
}
