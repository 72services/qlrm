package org.qlrm.test;

import org.junit.BeforeClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class JdbcBaseTest {

    protected static Connection con;

    @BeforeClass
    public static void init() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        con = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
        Statement stmt = con.createStatement();
        try {
            stmt.executeUpdate("DROP TABLE EMPLOYEE");
        } catch (Exception e) {
            // ignore
        }
        stmt.executeUpdate("CREATE TABLE EMPLOYEE (ID INTEGER NOT NULL, NAME VARCHAR, PRIMARY KEY (ID))");
        stmt.executeUpdate("INSERT INTO EMPLOYEE (ID , NAME) VALUES (1, 'Peter Muster')");
        stmt.close();
    }
}
