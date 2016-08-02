package org.qlrm.executor;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.qlrm.test.JdbcBaseTest;
import org.qlrm.to.EmployeeTO;

public class JdbcQueryExecutorTest extends JdbcBaseTest {

    @Test
    public void select() throws SQLException {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select.sql");

        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            System.out.println(rec);
        }
    }

    @Test
    public void selectWithOneParam() throws SQLException {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select_with_one_param.sql", 1);

        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            System.out.println(rec);
        }
    }

    @Test
    public void selectWithTwoParams() throws SQLException {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "select_with_two_params.sql", 1, "Peter Muster");

        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            System.out.println(rec);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongFilename() throws SQLException {
        JdbcQueryExecutor queryExecutor = new JdbcQueryExecutor();

        List<EmployeeTO> list = queryExecutor.executeSelect(con, EmployeeTO.class, "xy.sql", 1, "Peter Muster");

        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            System.out.println(rec);
        }
    }
}
