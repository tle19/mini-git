package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Commit {

    private String message;
    private String timestamp;
    private String parent;
    private Blob[] blobs;

    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        if (this.parent == null) {
            this.timestamp = "00:00:00 UTC, Thursday, 1 January 1970";
        }

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
