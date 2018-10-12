package Interfaces;

import java.sql.SQLException;

public interface ReadFileInterface {

    String initDatabase() throws SQLException;

    void getData(String filePath) throws SQLException;

}
