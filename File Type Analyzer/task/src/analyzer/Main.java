package analyzer;

import analyzer.fileIdentifierStrategy.*;

public class Main {
    public static void main(String[] args) {
        String db = "C:\\patterns.db";
        String directory = args[0];
        try(ContextFileIdentifier fileIdentifier = new ContextFileIdentifier(new FileIdentifierKMP(db))) {
            fileIdentifier.checkDirectoryFormat(directory);
            fileIdentifier.checkDirectoryFormat(directory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
