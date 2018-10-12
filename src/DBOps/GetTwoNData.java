package DBOps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GetTwoNData implements DbOperation {

    private Statement stmt;
    private int count = 0;
    private Connection conn;

    public GetTwoNData(String url) throws SQLException {

        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();

    }

    @Override
    public String initDatabase() {

        String sqlCommand = "delete from twon";
        try {
            stmt.executeQuery(sqlCommand);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Database Initialised";
    }

    @Override
    public void getData(String filePath) {

    }
}
