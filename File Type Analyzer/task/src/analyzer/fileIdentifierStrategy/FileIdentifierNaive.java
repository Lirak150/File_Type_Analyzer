package analyzer.fileIdentifierStrategy;

import analyzer.Pattern;
import analyzer.SQLHelper;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class FileIdentifierNaive implements FileIdentifierStrategy {
    private final SQLHelper helper;

    public FileIdentifierNaive(String fileName) {
        try {
            helper = new SQLHelper(fileName);
        } catch (SQLException e) {
            System.out.println("Something went wrong with database in constructor");
            e.printStackTrace();
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public void checkDirectoryFormat(File file) {
        List<Pattern> patterns = helper.getPatternStrings();
        for (int i = patterns.size() - 1; i >= 0; i--) {
            Pattern p = patterns.get(i);
            if (checkFormat(file, p.getPattern())) {
                System.out.println(file.getName() + ": " + p.getOutputString());
                return;
            }
        }
        System.out.println(file.getName() + ": " + UNKNOWN_FILE_TYPE);
    }

    private boolean checkFormat(File file, String pattern) {
        try {
            FileIdentifierStrategy.checkFile(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found or can't be open");
            e.printStackTrace();
        }
        try {
            BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
            byte[] bytesString = pattern.getBytes();
            byte[] buf = new byte[bytesString.length];

            inStream.read(buf);
            int k;
            while ((k = inStream.read()) != -1) {
                System.arraycopy(buf, 1, buf, 0, buf.length - 1);
                buf[buf.length - 1] = (byte) k;
                if (Arrays.equals(buf, bytesString)) {
                    inStream.close();
                    return true;
                }
            }
            inStream.close();
        } catch (IOException e) {
            System.out.println("Something went wrong because of file");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close(){
        try {
            helper.destroy();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
