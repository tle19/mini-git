package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Add implements Serializable {

    /** Folder of files that are staged */
    static final File INDEX = new File(".gitlet/index");
}
