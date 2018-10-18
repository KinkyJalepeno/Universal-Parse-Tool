package QueryDbTable;

import Interfaces.QueryTableInterface;
import javafx.scene.control.TextArea;

import java.sql.*;

public class QueryTwonTable implements QueryTableInterface {

    private Statement stmt;
    private Connection conn;
    private ResultSet rs;
    private String sqlCommand;

    public QueryTwonTable(String url) throws SQLException {

        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();

    }

    @Override
    public void getDataFromDb(TextArea textArea) throws SQLException {

        String simID;
        int totalMins;

        textArea.setText("DB query results....\n");

        for (int port = 0; port <= 31; port++) {

            textArea.appendText("\n");

            for (int pos = 1; pos <= 4; pos++) {

                sqlCommand = "SELECT scid, SUM(secs) FROM twon WHERE port = '" + port + "' AND position = '" + pos + "';";

                simID = simID();
                totalMins = Integer.parseInt(getTotalUsed());


                textArea.appendText("g" + port + " / " + pos + " - mins = " + totalMins +
                        "             \tSCID: " + simID + "\n");
            }
        }
        conn.close();
    }

    public String simID() {

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

    public String getTotalUsed() {

        String results;

        try {
            rs = stmt.executeQuery(sqlCommand);
            results = String.valueOf(rs.getInt(2));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return results;
    }
}
