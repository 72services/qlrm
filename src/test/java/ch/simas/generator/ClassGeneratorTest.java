package ch.simas.generator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClassGeneratorTest {

    private static Connection con;

    @BeforeClass
    public static void setUpClass() {
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE EMPLOYEE (ID INTEGER NOT NULL, NAME VARCHAR, PRIMARY KEY (ID))");
            stmt.close();
        } catch (Exception ex) {
            Logger.getLogger(ClassGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void generateFromTables() {
        try {
            ClassGenerator.generateFromTables("", null, null, false, con, "EMPLOYEE");
        } catch (Exception ex) {
            Logger.getLogger(ClassGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void generateFromResultSet() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NAME FROM EMPLOYEE");
            ClassGenerator.generateFromResultSet("", null, "EmployeeNameTO", false, rs);
            stmt.close();
        } catch (Exception ex) {
            Logger.getLogger(ClassGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}