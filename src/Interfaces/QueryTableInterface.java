package Interfaces;

import javafx.scene.control.TextArea;

import java.sql.SQLException;

public interface QueryTableInterface {

    void getDataFromDb(TextArea textArea) throws SQLException;

    String getTotalUsed();

    int results(String sqlCommand);

    String simID(String sqlCommand);


}
