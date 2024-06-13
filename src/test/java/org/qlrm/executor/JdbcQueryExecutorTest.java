package org.qlrm.executor;

import org.junit.jupiter.api.Test;
import org.qlrm.test.JdbcBaseTest;
import org.qlrm.to.EmployeeTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcQueryExecutorTest extends JdbcBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcQueryExecutorTest.class);

    @Test
    void select() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select.sql");

        assertNotNull(list);
        assertTrue(!list.isEmpty());

        LOGGER.debug(list.toString());
    }

    @Test
    void shouldEnsureDirectSqlApi() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, "SELECT ID, NAME FROM EMPLOYEE", EmployeeTO.class);

        assertNotNull(list);
        assertFalse(list.isEmpty());

        LOGGER.debug(list.toString());
    }

    @Test
    void selectWithOneParam() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select_with_one_param.sql", 1);

        assertNotNull(list);
        assertFalse(list.isEmpty());

        LOGGER.debug(list.toString());
    }

    @Test
    void selectWithTwoParams() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select_with_two_params.sql", 1, "Peter Muster");

        assertNotNull(list);
        assertFalse(list.isEmpty());

        LOGGER.debug(list.toString());
    }

    @Test
    void wrongFilename() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        assertThrows(IllegalArgumentException.class,
                () -> queryExecutor.executeSelect(con, EmployeeTO.class, "xy.sql", 1, "Peter Muster"));
    }
}
