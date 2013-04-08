package ch.simas.sqlresultmapper;

import ch.simas.generator.ClassGenerator;
import ch.simas.sqlresultmapper.model.Employee;
import ch.simas.sqlresultmapper.to.EmployeeTO;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

public class JpaSqlResultMapperTest {

    private static EntityManager em;

    @BeforeClass
    public static void init() throws ClassNotFoundException, SQLException, FileNotFoundException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("srm");
        em = emf.createEntityManager();
        EntityTransaction trx = em.getTransaction();
        trx.begin();
        Employee e = new Employee();
        e.setName("Peter Muster");
        em.persist(e);
        trx.commit();

        Class.forName("org.h2.Driver");
        Connection con = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
        ClassGenerator.generateFromTables("src/test/java/", "ch.simas.sqlresultmapper.to", "TO", false, con, "EMPLOYEE");
    }

    @Test
    public void listWithSql() {
        Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE");
        List<EmployeeTO> list = JpaSqlResultMapper.list(q, EmployeeTO.class);

        Assert.assertNotNull(list);
        for (EmployeeTO rec : list) {
            System.out.println(rec);
        }
    }

    @Test
    public void listWithJpql() {
        Query q = em.createQuery("SELECT e.id, e.name FROM Employee e");
        List<EmployeeTO> list = JpaSqlResultMapper.list(q, EmployeeTO.class);

        Assert.assertNotNull(list);
        for (EmployeeTO rec : list) {
            System.out.println(rec);
        }
    }

    @Test
    public void uniqueResultWithSql() {
        Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE WHERE ID = 1");
        EmployeeTO to = JpaSqlResultMapper.uniqueResult(q, EmployeeTO.class);

        Assert.assertNotNull(to);
        System.out.println(to);
    }

    @Test
    public void uniqueResultWithJpql() {
        Query q = em.createNativeQuery("SELECT e.id, e.name FROM Employee e WHERE e.id = 1");
        EmployeeTO to = JpaSqlResultMapper.uniqueResult(q, EmployeeTO.class);

        Assert.assertNotNull(to);
        System.out.println(to);
    }
}
