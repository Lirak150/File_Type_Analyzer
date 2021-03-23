package analyzer.fileIdentifierStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileIdentifierStrategy extends AutoCloseable {
    String UNKNOWN_FILE_TYPE = "Unknown file type";

    static void checkFile(File file) throws FileNotFoundException {
        if (file.isDirectory() || !file.exists() || !file.canRead()) {
            throw new FileNotFoundException();
        }
    }

    void checkDirectoryFormat(File file);

    default void checkDirectoryFormat(String pathToFile){
        checkDirectoryFormat(new File(pathToFile));
    }
}
