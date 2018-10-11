package DBOps;

import java.sql.*;

public class DatabaseQuery {

    private Statement stmt;
    private Connection conn;
    private ResultSet rs;

    public DatabaseQuery(String url) throws SQLException {

        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();

    }

    public int results(String sqlCommand) {
        int results;

        try {
            rs = stmt.executeQuery(sqlCommand);
            results = rs.getInt(2);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
        return results;
    }

    public String simID(String sqlCommand) {

        String simID;

        try {
            rs = stmt.executeQuery(sqlCommand);
            simID = rs.getString(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return simID;
    }
}
