package QueryDbTable;

import Interfaces.QueryTableInterface;
import javafx.scene.control.TextArea;

import java.sql.*;

public class QueryQuescomTable implements QueryTableInterface {

    private Statement stmt;
    private Connection conn;
    private int totalMins = 0;


    public QueryQuescomTable(String url) throws SQLException {

        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();


    }


    @Override
    public void getDataFromDb(TextArea textArea) throws SQLException {


        textArea.setText("DB query results....\n\n");
        String sqlCommand = "select imsi, sum(mins) from quescom group by imsi order by imsi asc;";

        ResultSet rs = stmt.executeQuery(sqlCommand);

        while (rs.next()) {

            int secs = rs.getInt(2);
            int mins = secs / 60;

            totalMins += secs / 60;

            //System.out.println(secs + " " + mins);

            textArea.appendText("IMSI: " + rs.getString(1) + "  " + "\t{Count: " + mins + "]\n");

        }
        //conn.close();
    }

    public int getTotalMins() {

        return totalMins;
    }


    @Override
    public String getTotalUsed() {

        return null;
    }


    @Override
    public String simID() {

        return null;
    }
}
