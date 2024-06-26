package org.qlrm.generator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.sql.*;

class ClassGeneratorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassGeneratorTest.class);

    private static Connection con;
    private static final ClassGenerator classGenerator = new ClassGenerator();

    @BeforeAll
    static void setUpClass() {
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
    void generateFromTables() {
        try {
            classGenerator.generateFromTables(System.getProperty("user.dir"), null, null, false, con, "EMPLOYEE");
        } catch (SQLException | FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Test
    void generateFromResultSet() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NAME FROM EMPLOYEE");
            classGenerator.generateFromResultSet(System.getProperty("user.dir"), null, "EmployeeWithName", false, rs);
            stmt.close();
        } catch (SQLException | FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
