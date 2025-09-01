package infra.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Db {
    private static final String URL = "jdbc:h2:mem:exam;DB_CLOSE_DELAY=-1";
    private Db() {}

    public static void init() throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        Statement st = null;
        try {
            st = c.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS model (id IDENTITY PRIMARY KEY, name VARCHAR(200) NOT NULL)");
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_model_name ON model(name)");
        } finally {
            if (st != null) st.close();
            c.close();
        }
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
