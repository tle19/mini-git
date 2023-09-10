package gitlet;

import java.io.Serializable;
import java.util.HashMap;

public class Storage implements Serializable {

    /** A hashmap storing the blobs. */
    private HashMap<String, Blob> _blobs;

    Storage() {
        _blobs = new HashMap<>();
    }

    public void put(String key, Blob val) {
        _blobs.put(key, val);
    }

    public void putAll(HashMap<String, Blob> map) {
        _blobs.putAll(map);
    }

    public Blob get(String key) {
        return _blobs.get(key);
    }

    public HashMap<String, Blob> getBlob() {
        return _blobs;
    }

    public void clear() {
        _blobs.clear();
    }

    public void remove(String key) {
        _blobs.remove(key);
    }

    public boolean contains(String key) {
        return _blobs.containsKey(key);
    }

}
