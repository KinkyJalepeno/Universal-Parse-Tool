package DBOps;

import java.sql.SQLException;

public interface DbOperation {

    String initDatabase() throws SQLException;

    void getData(String filepath) throws SQLException;

}
