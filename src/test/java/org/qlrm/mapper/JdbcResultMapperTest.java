package org.qlrm.mapper;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.qlrm.test.JdbcBaseTest;
import org.qlrm.to.EmployeeTO;

public class JdbcResultMapperTest extends JdbcBaseTest {

    private static final JdbcResultMapper jdbcResultMapper = new JdbcResultMapper();

    @Test
    public void testSql() throws SQLException {
        Statement stmt = con.createStatement();
        boolean ok = stmt.execute("SELECT ID, NAME FROM EMPLOYEE");
        Assert.assertTrue(ok);

        List<EmployeeTO> list = jdbcResultMapper.list(stmt.getResultSet(), EmployeeTO.class);
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);

        for (EmployeeTO rec : list) {
            System.out.println(rec);
        }
    }

    @Test
    public void uniqueResult() throws SQLException {
        Statement stmt = con.createStatement();
        boolean ok = stmt.execute("SELECT ID, NAME FROM EMPLOYEE WHERE ID = 1");
        Assert.assertTrue(ok);

        EmployeeTO to = jdbcResultMapper.uniqueResult(stmt.getResultSet(), EmployeeTO.class);
        Assert.assertNotNull(to);

        System.out.println(to);
    }
}
