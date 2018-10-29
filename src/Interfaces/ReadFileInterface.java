package Interfaces;

import java.sql.SQLException;

public interface ReadFileInterface {

    String initDatabase() throws SQLException;

    void getData(String filePath) throws SQLException;

    void addToBatch(String[] dataArray) throws SQLException;

    void executeBatch() throws SQLException;

}
