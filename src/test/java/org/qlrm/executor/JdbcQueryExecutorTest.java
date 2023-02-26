package org.qlrm.executor;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.qlrm.test.JdbcBaseTest;
import org.qlrm.to.EmployeeTO;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcQueryExecutorTest extends JdbcBaseTest {

    private static final Logger LOGGER = LogManager.getLogger(JdbcQueryExecutorTest.class);

    @Test
    void select() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select.sql");

        assertNotNull(list);
        assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    void shouldEnsureDirectSqlApi() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, "SELECT ID, NAME FROM EMPLOYEE", EmployeeTO.class);

        assertNotNull(list);
        assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    void selectWithOneParam() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select_with_one_param.sql", 1);

        assertNotNull(list);
        assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    void selectWithTwoParams() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select_with_two_params.sql", 1, "Peter Muster");

        assertNotNull(list);
        assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    void wrongFilename() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        assertThrows(IllegalArgumentException.class,
                () -> queryExecutor.executeSelect(con, EmployeeTO.class, "xy.sql", 1, "Peter Muster"));
    }
}
