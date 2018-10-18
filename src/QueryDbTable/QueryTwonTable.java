package QueryDbTable;

import Interfaces.QueryTableInterface;
import javafx.scene.control.TextArea;

import java.sql.*;

public class QueryTwonTable implements QueryTableInterface {

    private Statement stmt;
    private Connection conn;
    private ResultSet rs;

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

                String sqlCommand = "SELECT scid, SUM(secs) FROM twon WHERE port = '" +
                        port + "' AND position = '" + pos + "';";

                simID = simID(sqlCommand);
                totalMins = getTotalUsed(sqlCommand);


                textArea.appendText("g" + port + " / " + pos + " - mins = " + totalMins +
                        "             \tSCID: " + simID + "\n");
            }
        }
        conn.close();
    }

    public int getTotalUsed(String sqlCommand) {
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

    @Override
    public int results(String sqlCommand) {
        return 0;
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

    public String getTotalUsed() {
        return null;
    }
}
