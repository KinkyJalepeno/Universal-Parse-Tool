package ReadFileIntoDb;

import Interfaces.ReadFileInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class ReadHyperDataIntoDb implements ReadFileInterface {

    private Statement stmt;
    private static int count = 0;
    private Connection conn;
    private int batchCount = 0;

    public ReadHyperDataIntoDb(String url) throws SQLException {

        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();

    }

    @Override
    public String initDatabase() {

        String sqlCommand = "delete from hyper";
        try {
            stmt.executeQuery(sqlCommand);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Database Initialised";
    }


    public void getData(String filePath) throws SQLException {

        try {
            BufferedReader readIn = new BufferedReader(new FileReader(filePath));

            String text;
            while ((text = readIn.readLine()) != null) {
                count++;

                if (text.contains("confirmation")) {
                    continue;
                }else if(text.equals("")){
                    count --;
                    continue;
                }
                String dataArray[] = text.split("[|]");

                addToBatch(dataArray);

            }

        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
        }
        executeBatch();

    }

    public void addToBatch(String[] dataArray) throws SQLException {

        batchCount ++;
        conn.setAutoCommit(false);

        String card = (dataArray[1]);
        String port = (dataArray[2]);
        String result = (dataArray[5]);
        String smsLength = (dataArray[11].substring(4));
        String scid = dataArray[13];
        String simPosition = (dataArray[14]);

        String sqlCommand = "INSERT INTO hyper VALUES ('" + card + "','" + port + "','" + result + "','" + smsLength +
                "','" + scid + "','" + simPosition + "');";

        stmt.addBatch(sqlCommand);
        if(batchCount >= 1000){
            batchCount = 0;
            stmt.executeBatch();
        }


    }

    public void executeBatch() throws SQLException {

        stmt.executeBatch();
        conn.commit();
        System.out.println("batch write to DB complete");
        conn.close();
    }

    public static int getCount() {

        return count;
    }

}
