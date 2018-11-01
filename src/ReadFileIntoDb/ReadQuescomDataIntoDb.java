package ReadFileIntoDb;

import Interfaces.ReadFileInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadQuescomDataIntoDb implements ReadFileInterface {

    private Statement stmt;
    private Connection conn;
    private int count = 0;
    private int totalSecs = 0;


    public ReadQuescomDataIntoDb(String url) throws SQLException {

        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();

    }

    @Override
    public String initDatabase() {

        String sqlCommand = "delete from quescom";

        try {
            stmt.executeQuery(sqlCommand);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return "Database initialised";

    }

    @Override
    public void getData(String filePath) throws SQLException {


        String text;

        try {
            BufferedReader readIn = new BufferedReader(new FileReader(filePath));

            while ((text = readIn.readLine()) != null) {
                count++;

                String[] dataArray = text.split("[;]");

                if ((dataArray[23]).equals("") || dataArray[14].equals("0")) {

                    continue;
                } else {
                    addToBatch(dataArray);

                }

            }
        } catch (IOException e) {
            System.out.println("No such document");
        }
        executeBatch();
    }


    @Override
    public void addToBatch(String[] dataArray) throws SQLException {

        conn.setAutoCommit(false);

        int secs = Integer.parseInt(dataArray[14]);
        String imsi = dataArray[23];

        totalSecs += secs;
        //System.out.println(imsi + " " + secs);

        String sqlCommand = "INSERT into quescom VALUES ('" + imsi + "','" + secs + "');";

        stmt.addBatch(sqlCommand);
    }

    @Override
    public void executeBatch() throws SQLException {

        stmt.executeBatch();
        conn.commit();
        System.out.println("batch write to DB complete");
        conn.close();

    }

    public int getCount() {

        return count;
    }

    public int getTotalSecs() {

        return totalSecs;
    }
}
