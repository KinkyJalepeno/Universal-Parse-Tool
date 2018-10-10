package DBOps;

import java.sql.*;

public class GetHyperData implements DbOperation {

    private String url;
    private Connection conn;
    private Statement stmt;
    private PreparedStatement ps;

    public GetHyperData(String url) throws SQLException {

        this.url = url;
        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();

    }

    @Override
    public String initDatabase() throws SQLException {

        String sqlCommand ="delete from hyper";
        ps = conn.prepareStatement(sqlCommand);

        try{
            stmt.executeQuery(sqlCommand);


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return "Database Initialised";
    }


    public void getData() {

    }
}
