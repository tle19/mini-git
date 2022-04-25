package gitlet;

import java.util.HashMap;

public class Storage {

    private HashMap<String, String> _blobs;

    Storage() {
        _blobs = new HashMap<>();
    }

    public void put(String key, String val) {
        _blobs.put(key, val);
    }

    public void putAll(HashMap<String, String> map) {
        _blobs.putAll(map);
    }

}
