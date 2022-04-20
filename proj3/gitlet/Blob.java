package gitlet;

import java.io.Serializable;
import java.io.File;

public class Blob implements Serializable {

    private byte[] _blob;

    private String _name;

    Blob(File file) {
        _blob = Utils.readContents(file);
        _name = Utils.sha1(_blob);
    }

    public String getName() {
        return _name;
    }

    public byte[] getBlob() {
        return _blob;
    }
}
