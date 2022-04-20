package gitlet;

import java.io.Serializable;
import java.io.File;

public class Blob implements Serializable {

    private byte[] _blob;

    private String _name;

    private String _filename;

    Blob(File file, String filename) {
        _blob = Utils.readContents(file);
        _name = Utils.sha1(_blob);
        _filename = filename;
    }

    public String getName() {
        return _name;
    }

    public byte[] getBlob() {
        return _blob;
    }

    public String getFileName() {
        return _filename;
    }
}
