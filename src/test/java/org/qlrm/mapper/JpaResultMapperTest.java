package org.qlrm.mapper;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qlrm.generator.ClassGenerator;
import org.qlrm.model.Employee;
import org.qlrm.to.EmployeeTO;

public class JpaResultMapperTest {

    private static EntityManager em;
    private static JpaResultMapper jpaResultMapper = new JpaResultMapper();
    private static ClassGenerator classGenerator = new ClassGenerator();
	private static int employeeId;

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
		employeeId = e.getId();

        // FIXME stefanheimberg: deaktiviert weil ohne generierte TO Objekte kompiliert dieser Test auch nicht.
        // generierung deaktiviert
        //Class.forName("org.h2.Driver");
        //Connection con = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
        //classGenerator.generateFromTables("src/test/java/", "org.qlrm.to", "TO", false, con, "EMPLOYEE");
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
	public void listWithJpqlWhenUniqueResult() {
		Query q = em.createQuery("SELECT e.id FROM Employee e");
		List<Long> list = jpaResultMapper.list(q, Long.class);

		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(employeeId, list.get(0).longValue());
	}

	@Test
	public void listWithJpqlWithNoResult() {
		Query q = em.createQuery("SELECT e FROM Employee e WHERE e.id=?1");
		q.setParameter(1, employeeId + 1);

		List<Long> list = jpaResultMapper.list(q, Long.class);

		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}

    @Test
    public void uniqueResultWithSql() {
        Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE WHERE ID = 1");
        EmployeeTO to = jpaResultMapper.uniqueResult(q, EmployeeTO.class);

        Assert.assertNotNull(to);
        System.out.println(to);
    }

    @Test
	public void uniqueResultWithJpqlWhenSingleRow() {
		Query q = em.createNativeQuery("SELECT e.id, e.name FROM Employee e WHERE e.id = 1");
		EmployeeTO to = jpaResultMapper.uniqueResult(q, EmployeeTO.class);

		Assert.assertNotNull(to);
		System.out.println(to);
	}

	@Test
	public void uniqueResultWithJpqlWhenSingleResult() {
		Query q = em.createQuery("SELECT COUNT(e) FROM Employee e");
		Long result = jpaResultMapper.uniqueResult(q, Long.class);

		Assert.assertNotNull(result);
		Assert.assertEquals(employeeId, result.longValue());
    }

	@Test(expected = NoResultException.class)
	public void uniqueResultWithJpqlWhenNoResult() {
		Query q = em.createQuery("SELECT e FROM Employee e WHERE e.id=?1");
		q.setParameter(1, employeeId + 1);

		jpaResultMapper.uniqueResult(q, Long.class);
		Assert.fail("Expected " + NoResultException.class.getSimpleName() + " but not exception was thrown.");
	}
}
