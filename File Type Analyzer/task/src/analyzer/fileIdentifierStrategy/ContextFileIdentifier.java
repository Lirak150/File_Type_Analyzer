package analyzer.fileIdentifierStrategy;

import java.io.File;
import java.io.IOException;

public class ContextFileIdentifier implements AutoCloseable {

    private FileIdentifierStrategy strategy;

    public ContextFileIdentifier(FileIdentifierStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(FileIdentifierStrategy strategy) {
        this.strategy = strategy;
    }

    public void checkDirectoryFormat(File file) {
        strategy.checkDirectoryFormat(file);
    }

    public void checkDirectoryFormat(String pathToFile) {
        strategy.checkDirectoryFormat(pathToFile);
    }

    @Override
    public void close() throws Exception {
        strategy.close();
    }
}
