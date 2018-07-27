package org.qlrm.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.qlrm.model.Employee;
import org.qlrm.test.JpaBaseTest;
import org.qlrm.to.EmployeeTO;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class JpaResultMapperTest extends JpaBaseTest {

    private static final Logger LOGGER = LogManager.getLogger(JpaResultMapperTest.class);

    private final JpaResultMapper jpaResultMapper = new JpaResultMapper();

    @Test
    public void listWithSql() {
        Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE");
        List<EmployeeTO> list = jpaResultMapper.list(q, EmployeeTO.class);

        Assert.assertNotNull(list);
        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    public void listWithJpql() {
        Query q = em.createQuery("SELECT e.id, e.name FROM Employee e");
        List<EmployeeTO> list = jpaResultMapper.list(q, EmployeeTO.class);

        Assert.assertNotNull(list);
        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    public void listWithJpqlWhenUniqueResult() {
        Query q = em.createQuery("SELECT e.id FROM Employee e");
        List<Integer> list = jpaResultMapper.list(q, Integer.class);

        Assert.assertNotNull(list);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(employeeId, list.get(0).intValue());
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
        Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE WHERE ID = ?");
        q.setParameter(1, employeeId);

        EmployeeTO to = jpaResultMapper.uniqueResult(q, EmployeeTO.class);

        Assert.assertNotNull(to);
        LOGGER.debug(to);
    }

    @Test
    public void uniqueResultWithJpqlWhenSingleRow() {
        Query q = em.createNativeQuery("SELECT e.id, e.name FROM Employee e WHERE e.id = ?");
        q.setParameter(1, employeeId);
        EmployeeTO to = jpaResultMapper.uniqueResult(q, EmployeeTO.class);

        Assert.assertNotNull(to);
        LOGGER.debug(to);
    }

    @Test
    public void uniqueResultWithJpqlWhenSingleResult() {
        Query q = em.createQuery("SELECT COUNT(e) FROM Employee e");
        Long result = jpaResultMapper.uniqueResult(q, Long.class);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.longValue());
    }

    @Test(expected = NoResultException.class)
    public void uniqueResultWithJpqlWhenNoResult() {
        Query q = em.createQuery("SELECT e FROM Employee e WHERE e.id = ?1");
        q.setParameter(1, employeeId + 1);

        jpaResultMapper.uniqueResult(q, Long.class);
        Assert.fail("Expected " + NoResultException.class.getSimpleName() + " but no exception was thrown.");
    }

    /**
     * Tests if the constructor search algorithm returns the correct
     * constructor. There has been a bug where the algorithm just returned the
     * first constructor (if multiple constructors where available) who's
     * argument count did match the result row column count.
     */
    @Test
    public void testWhenTargetTypeHasMultipleConstructorsWithSameArgumentCount() {
        Query q = em.createQuery("SELECT e.name FROM Employee e WHERE e.id = ?1");
        q.setParameter(1, employeeId);

        List<String> result = jpaResultMapper.list(q, String.class);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(emplyoeeName, result.get(0));
    }

    @Test
    public void testNullResultColumnRaisesNPE() {
        Employee employeeWithNoName = new Employee();
        storeEmployee(employeeWithNoName);
        Query q = em.createQuery("SELECT e.name FROM Employee e WHERE e.id = ?1");
        q.setParameter(1, employeeWithNoName.getId());

        try {
            jpaResultMapper.list(q, String.class);
            Assert.fail("Expected exception has not been thrown.");
        } catch (RuntimeException e) {
            Assert.assertEquals(NullPointerException.class, e.getCause().getClass());
        }
    }

}
