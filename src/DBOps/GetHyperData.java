package DBOps;

import java.sql.*;

public class GetHyperData implements DbOperation {

    private Connection conn;
    private Statement stmt;


    public GetHyperData(String url) throws SQLException {

        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();

    }

    @Override
    public String initDatabase() {

        String sqlCommand ="delete from hyper";
        try{
            stmt.executeQuery(sqlCommand);


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return "Database Initialised";
    }


    public void getData(String filePath) {

        System.out.println("Get the data from file");

    }
}
