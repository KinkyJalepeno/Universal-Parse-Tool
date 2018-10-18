package ReadFileIntoDb;

import Interfaces.ReadFileInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class ReadTwonDataIntoDb implements ReadFileInterface {

    private Statement stmt;
    private static int count;
    private static int totalSecs;
    private static int grandTotal;
    private Connection conn;


    public ReadTwonDataIntoDb(String url) throws SQLException {

        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();

        totalSecs = 0;
        grandTotal = 0;
        count = 0;

    }

    public static int getCount() {
        return count;
    }

    public static int getGrandTotal() {
        return grandTotal;
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
    public void getData(String filePath) throws SQLException {

        String text;

        try {
            BufferedReader readIn = new BufferedReader(new FileReader(filePath));

            while ((text = readIn.readLine()) != null) {
                count++;
                if (text.startsWith("**") && (text.contains("O-OK"))) {

                    String[] dataArray = text.split("[/\\s:]");
                    addToBatch(dataArray);
                }
            }
        } catch (IOException | SQLException e) {
            System.out.println("No such document");
        }
        executeBatch();
    }


    private void addToBatch(String[] dataArray) throws SQLException {

        conn.setAutoCommit(false);

        String port = dataArray[9].substring(1);
        int mins = Integer.parseInt(dataArray[16]);
        int secs = Integer.parseInt(dataArray[17]);
        int position = Integer.parseInt(dataArray[25]);
        String scid = dataArray[26];

        totalSecs = ((mins * 60) + secs);
        grandTotal += totalSecs;

        String sqlCommand = "INSERT INTO twon VALUES ('" + port + "','" + totalSecs + "','" + position + "','" + scid + "');";

        stmt.addBatch(sqlCommand);

    }


    private void executeBatch() throws SQLException {

        stmt.executeBatch();
        conn.commit();
        System.out.println("batch write to DB complete");
        conn.close();
    }

}
