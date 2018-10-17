package ReadFileIntoDb;

import Interfaces.ReadFileInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadTwonDataIntoDb implements ReadFileInterface {

    private Statement stmt;
    private int count = 0;
    private Connection conn;
    private int nulls = 0;

    public ReadTwonDataIntoDb(String url) throws SQLException {

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

        try {
            BufferedReader readIn = new BufferedReader(new FileReader(filePath));

            String text;

            while (nulls <= 10) {
                text = readIn.readLine();

                if (text.startsWith("**")) {

                    addToBatch(text);
                } else {
                    nulls++;
                }
            }
        } catch (IOException e) {
            System.out.println("No such document");

        } catch (NullPointerException e2) {
            System.out.println("End of document");

        }
    }

    private void addToBatch(String text) {

        System.out.println("text = " + text);
        nulls = 0;
        // TODO Split the line with a REGEX and drop into string array
    }
}
