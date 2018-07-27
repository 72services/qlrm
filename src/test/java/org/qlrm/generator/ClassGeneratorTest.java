package org.qlrm.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.*;

public class ClassGeneratorTest {

    private static final Logger LOGGER = LogManager.getLogger(ClassGeneratorTest.class);

    private static Connection con;
    private static ClassGenerator classGenerator = new ClassGenerator();

    @BeforeClass
    public static void setUpClass() {
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE EMPLOYEE (ID INTEGER NOT NULL, NAME VARCHAR, PRIMARY KEY (ID))");
            stmt.close();
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    // This test tests the deprecated method itself. Warning is not necessary.
    @SuppressWarnings("deprecation")
    @Test
    public void generateFromTables() {
        try {
            classGenerator.generateFromTables(System.getProperty("user.dir"), null, null, false, con, "EMPLOYEE");
        } catch (SQLException | FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Test
    public void generateFromResultSet() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NAME FROM EMPLOYEE");
            classGenerator.generateFromResultSet(System.getProperty("user.dir"), null, "EmployeeNameTO", false, rs);
            stmt.close();
        } catch (SQLException | FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
