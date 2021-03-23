package analyzer.fileIdentifierStrategy;

import analyzer.Pattern;
import analyzer.SQLHelper;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileIdentifierKMP implements FileIdentifierStrategy {
    private final ExecutorService exService =
            Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors()
            );
    private final SQLHelper helper;

    public FileIdentifierKMP(String fileName) {
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
        if (file.isFile()) {
            exService.execute(() -> {
                List<Pattern> patterns = helper.getPatternStrings();
                for (int i = patterns.size() - 1; i >= 0; i--) {
                    Pattern p = patterns.get(i);
                    if (checkFileFormat(file, p.getPattern())) {
                        System.out.println(file.getName() + ": " + p.getOutputString());
                        return;
                    }
                }
                System.out.println(file.getName() + ": " + UNKNOWN_FILE_TYPE);
            });
        }

        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File tempFile : listFiles) {
                if (tempFile != null) {
                    checkDirectoryFormat(tempFile);
                }
            }
        }
    }

    private boolean checkFileFormat(File file, String pattern) {
        try {
            FileIdentifierStrategy.checkFile(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found or can't be open");
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            int[] prefixFunction = prefixFunction(pattern);
            int k;
            int l = 0;
            while ((k = reader.read()) != -1) {
                char c = (char) k;
                if (pattern.charAt(l) == c) {
                    l++;
                    if (l == pattern.length()) {
                        return true;
                    }
                } else if (l > 0) {
                    while (l > 0 && pattern.charAt(l) != c) {
                        l = prefixFunction[l - 1];
                    }
                    if (pattern.charAt(l) == c) {
                        l++;
                    }
                }
            }
            System.out.println(file.getName() + ": " + UNKNOWN_FILE_TYPE);
        } catch (IOException e) {
            System.out.println("Something went wrong because of file");
            e.printStackTrace();
        }
        return false;
    }

    private int[] prefixFunction(String pattern) {
        int[] prefix = new int[pattern.length()];
        prefix[0] = 0;

        for (int i = 1; i < prefix.length; i++) {
            int j = prefix[i - 1];
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = prefix[j - 1];
            }

            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            prefix[i] = j;
        }
        return prefix;
    }

    @Override
    public void close() {
        try {
            helper.destroy();
            exService.shutdown();
            exService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }
}
