package org.qlrm.mapper;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.qlrm.model.Employee;
import org.qlrm.test.JpaBaseTest;
import org.qlrm.to.EmployeeTO;
import org.qlrm.to.SingleColumnTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class JpaResultMapperTest extends JpaBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaResultMapperTest.class);

    private final JpaResultMapper jpaResultMapper = new JpaResultMapper();

    @Test
    void listWithSql() {
        Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE");
        List<EmployeeTO> list = jpaResultMapper.list(q, EmployeeTO.class);

        assertNotNull(list);

        LOGGER.debug(list.toString());
    }

    @Test
    void listWithSqlOnlyOneColumn() {
        Query q = em.createNativeQuery("SELECT NAME FROM EMPLOYEE");
        List<SingleColumnTO> list = jpaResultMapper.list(q, SingleColumnTO.class);

        assertNotNull(list);

        LOGGER.debug(list.toString());
    }

    @Test
    void listWithSqlOnlyOneColumnMultipleRows() {
        Employee secondEmployee = new Employee();
        secondEmployee.setName("Sarah Meier");
        storeEmployee(secondEmployee);

        Query q = em.createNativeQuery("SELECT NAME FROM EMPLOYEE");
        List<SingleColumnTO> list = jpaResultMapper.list(q, SingleColumnTO.class);

        assertNotNull(list);

        LOGGER.debug(list.toString());
    }

    @Test
    void listWithJpql() {
        Query q = em.createQuery("SELECT e.id, e.name FROM Employee e");
        List<EmployeeTO> list = jpaResultMapper.list(q, EmployeeTO.class);

        assertNotNull(list);

        LOGGER.debug(list.toString());
    }

    @Test
    void listWithJpqlWhenUniqueResult() {
        Query q = em.createQuery("SELECT e.id FROM Employee e");
        List<Integer> list = jpaResultMapper.list(q, Integer.class);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(employeeId, list.get(0).intValue());
    }

    @Test
    void listWithJpqlWithNoResult() {
        Query q = em.createQuery("SELECT e FROM Employee e WHERE e.id=?1");
        q.setParameter(1, employeeId + 1);

        List<Long> list = jpaResultMapper.list(q, Long.class);

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void uniqueResultWithSql() {
        Query q = em.createNativeQuery("SELECT ID, NAME FROM EMPLOYEE WHERE ID = ?");
        q.setParameter(1, employeeId);

        EmployeeTO to = jpaResultMapper.uniqueResult(q, EmployeeTO.class);

        assertNotNull(to);

        LOGGER.debug(to.toString());
    }

    @Test
    void uniqueResultWithJpqlWhenSingleRow() {
        Query q = em.createNativeQuery("SELECT e.id, e.name FROM Employee e WHERE e.id = ?");
        q.setParameter(1, employeeId);
        EmployeeTO to = jpaResultMapper.uniqueResult(q, EmployeeTO.class);

        assertNotNull(to);

        LOGGER.debug(to.toString());
    }

    @Test
    void uniqueResultWithJpqlWhenSingleResult() {
        Query q = em.createQuery("SELECT COUNT(e) FROM Employee e");
        Long result = jpaResultMapper.uniqueResult(q, Long.class);

        assertNotNull(result);
        assertEquals(1, result.longValue());
    }

    @Test
    void uniqueResultWithJpqlWhenNoResult() {
        Query q = em.createQuery("SELECT e FROM Employee e WHERE e.id = ?1");
        q.setParameter(1, employeeId + 1);

        assertThrows(NoResultException.class, () -> jpaResultMapper.uniqueResult(q, Long.class));
    }

    /**
     * Tests if the constructor search algorithm returns the correct
     * constructor. There has been a bug where the algorithm just returned the
     * first constructor (if multiple constructors where available) whose
     * argument count did match the result row column count.
     */
    @Test
    void testWhenTargetTypeHasMultipleConstructorsWithSameArgumentCount() {
        Query q = em.createQuery("SELECT e.name FROM Employee e WHERE e.id = ?1");
        q.setParameter(1, employeeId);

        List<String> result = jpaResultMapper.list(q, String.class);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(employeeName, result.get(0));
    }

    @Test
    void testNullResultColumnRaisesNPE() {
        Employee employeeWithNoName = new Employee();
        storeEmployee(employeeWithNoName);
        Query q = em.createQuery("SELECT e.name FROM Employee e WHERE e.id = ?1");
        q.setParameter(1, employeeWithNoName.getId());

        try {
            jpaResultMapper.list(q, String.class);
            fail("Expected exception has not been thrown.");
        } catch (RuntimeException e) {
            assertEquals(NullPointerException.class, e.getCause().getClass());
        }
    }

}
