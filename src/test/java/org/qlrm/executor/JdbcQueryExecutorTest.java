package org.qlrm.executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.qlrm.test.JdbcBaseTest;
import org.qlrm.to.EmployeeTO;

import java.util.List;

public class JdbcQueryExecutorTest extends JdbcBaseTest {

    private static final Logger LOGGER = LogManager.getLogger(JdbcQueryExecutorTest.class);

    @Test
    public void select() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select.sql");

        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    public void selectWithOneParam() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select_with_one_param.sql", 1);

        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test
    public void selectWithTwoParams() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select_with_two_params.sql", 1, "Peter Muster");

        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongFilename() {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "xy.sql", 1, "Peter Muster");

        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            LOGGER.debug(rec);
        }
    }
}
