package org.qlrm.executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.qlrm.test.JpaBaseTest;
import org.qlrm.to.EmployeeTO;
import org.qlrm.to.SingleColumnTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JpaQueryExecutorTest extends JpaBaseTest {

    private static final Logger LOGGER = LogManager.getLogger(JpaQueryExecutorTest.class);

    @Test
    public void select() {
        JpaQueryExecutor queryExecutor = new JpaQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(em, EmployeeTO.class, "select.sql");

        assertNotNull(list);
        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    public void selectWithPaging() {
        JpaQueryExecutor queryExecutor = new JpaQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(em, EmployeeTO.class, "select.sql", new PageRequest(0, 20));

        assertNotNull(list);
        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    public void selectWithOneParam() {
        JpaQueryExecutor queryExecutor = new JpaQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(em, EmployeeTO.class, "select_with_one_param.sql", 1);

        assertNotNull(list);
        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    void selectWithTwoParams() {
        JpaQueryExecutor queryExecutor = new JpaQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(em, EmployeeTO.class, "select_with_two_params.sql", 1, "Peter Muster");

        assertNotNull(list);
        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    void selectOnlyOneColumn() {
        JpaQueryExecutor queryExecutor = new JpaQueryExecutor();

        List<SingleColumnTO> list = queryExecutor.executeSelect(em, SingleColumnTO.class, "select_with_one_column.sql");

        assertFalse(list.isEmpty());
        for (SingleColumnTO rec : list) {
            LOGGER.debug(rec);
        }
    }
}
