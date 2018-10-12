package ReadFileIntoDb;

import Interfaces.QueryTableInterface;
import javafx.scene.control.TextArea;

import java.sql.*;

public class QueryHyperTable implements QueryTableInterface {

    private Statement stmt;
    private Connection conn;
    private ResultSet rs;
    private int totalSent;

    public QueryHyperTable(String url) throws SQLException {

        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();

    }

    public void getDataFromDb(TextArea textArea) throws SQLException {

        int numberSent;
        String simID;

        textArea.setText("DB Query Results.....\n");

        for (int card = 21; card <= 28; card++) {

            for (int port = 1; port <= 4; port++) {

                textArea.appendText("\n");
                for (int pos = 1; pos <= 4; pos++) {

                    String sqlCommand = "SELECT scid, SUM(length) FROM hyper WHERE card = '" + card + "' AND port = '" +
                            port + "' AND position = '" + pos + "';";

                    simID = simID(sqlCommand);
                    numberSent = results(sqlCommand);

                    textArea.appendText(card + " / " + port + " / " + pos + " - [count = " + numberSent +
                            "]        \t[SCID: " + simID + "]\n");

                    totalSent += numberSent;
                }
            }
        }
        conn.close();

    }

    public String getTotalUsed() {

        return String.valueOf(totalSent);
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
