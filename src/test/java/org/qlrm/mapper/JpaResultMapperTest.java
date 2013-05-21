package org.qlrm.mapper;

import org.qlrm.generator.ClassGenerator;
import org.qlrm.model.Employee;
import org.qlrm.to.EmployeeTO;
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

public class JpaResultMapperTest {

    private static EntityManager em;
    private static JpaResultMapper jpaResultMapper = new JpaResultMapper();
    private static ClassGenerator classGenerator = new ClassGenerator();

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
        classGenerator.generateFromTables("src/test/java/", "org.qlrm.to", "TO", false, con, "EMPLOYEE");
    }

    @Test
    public void listWithSql() {
        Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE");
        List<EmployeeTO> list = jpaResultMapper.list(q, EmployeeTO.class);

        Assert.assertNotNull(list);
        for (EmployeeTO rec : list) {
            System.out.println(rec);
        }
    }

    @Test
    public void listWithJpql() {
        Query q = em.createQuery("SELECT e.id, e.name FROM Employee e");
        List<EmployeeTO> list = jpaResultMapper.list(q, EmployeeTO.class);

        Assert.assertNotNull(list);
        for (EmployeeTO rec : list) {
            System.out.println(rec);
        }
    }

    @Test
    public void uniqueResultWithSql() {
        Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE WHERE ID = 1");
        EmployeeTO to = jpaResultMapper.uniqueResult(q, EmployeeTO.class);

        Assert.assertNotNull(to);
        System.out.println(to);
    }

    @Test
    public void uniqueResultWithJpql() {
        Query q = em.createNativeQuery("SELECT e.id, e.name FROM Employee e WHERE e.id = 1");
        EmployeeTO to = jpaResultMapper.uniqueResult(q, EmployeeTO.class);

        Assert.assertNotNull(to);
        System.out.println(to);
    }
}
