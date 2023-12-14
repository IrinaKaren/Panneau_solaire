package dbaccess; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PGSQLConnection {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/panneau_solaire";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "root";

    // Register the PostgreSQL JDBC driver during class initialization
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC driver not found. Make sure it's in your classpath.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
}
