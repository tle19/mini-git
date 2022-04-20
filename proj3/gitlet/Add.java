package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Add implements Serializable {

    /** Folder of files that are staged */
    static final File INDEX = new File(".gitlet/index");

    /** Hashmap of blobs. */
    private HashMap<String, String> _blobs;

    Add() {
        _blobs = new HashMap<String, String>();
    }

    public void add(String name, String id) {
        _blobs.put(name, id);
    }
}
