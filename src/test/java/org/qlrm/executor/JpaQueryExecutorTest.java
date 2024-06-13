package org.qlrm.executor;

import org.junit.jupiter.api.Test;
import org.qlrm.test.JpaBaseTest;
import org.qlrm.to.EmployeeTO;
import org.qlrm.to.SingleColumnTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JpaQueryExecutorTest extends JpaBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaQueryExecutorTest.class);

    @Test
    public void select() {
        JpaQueryExecutor queryExecutor = new JpaQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(em, EmployeeTO.class, "select.sql");

        assertNotNull(list);

        LOGGER.debug(list.toString());
    }

    @Test
    public void selectWithPaging() {
        JpaQueryExecutor queryExecutor = new JpaQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(em, EmployeeTO.class, "select.sql", new PageRequest(0, 20));

        assertNotNull(list);

        LOGGER.debug(list.toString());
    }

    @Test
    public void selectWithOneParam() {
        JpaQueryExecutor queryExecutor = new JpaQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(em, EmployeeTO.class, "select_with_one_param.sql", 1);

        assertNotNull(list);

        LOGGER.debug(list.toString());
    }

    @Test
    void selectWithTwoParams() {
        JpaQueryExecutor queryExecutor = new JpaQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(em, EmployeeTO.class, "select_with_two_params.sql", 1, "Peter Muster");

        assertNotNull(list);

        LOGGER.debug(list.toString());
    }

    @Test
    void selectOnlyOneColumn() {
        JpaQueryExecutor queryExecutor = new JpaQueryExecutor();

        List<SingleColumnTO> list = queryExecutor.executeSelect(em, SingleColumnTO.class, "select_with_one_column.sql");

        assertFalse(list.isEmpty());

        LOGGER.debug(list.toString());
    }
}
