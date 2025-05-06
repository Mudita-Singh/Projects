
package sports_tracker;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Connect {
    public Connection c;
    public Statement s;

    public Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:///sports", "root", "LUCIF3R_7");
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
