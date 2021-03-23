package analyzer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLHelper {
    private String url;
    private Connection connection;
    private final List<Pattern> patterns;

    public SQLHelper(String fileName) throws SQLException {
        url = "jdbc:sqlite:" + fileName;
        connection = DriverManager.getConnection(url);

        patterns = new ArrayList<>();

        Statement st = connection.createStatement();
        ResultSet patternStrings = st.executeQuery("SELECT * FROM patterns ORDER BY id");
        while(patternStrings.next()){
            int id = patternStrings.getInt("id");
            String pattern = patternStrings.getString("pattern");
            String outputString = patternStrings.getString("outputString");
            patterns.add(new Pattern(pattern, outputString, id));
        }
        st.close();
        patternStrings.close();
    }

    public List<Pattern> getPatternStrings(){
        return patterns;
    }

    public void destroy() throws SQLException {
        connection.close();
    }
}
