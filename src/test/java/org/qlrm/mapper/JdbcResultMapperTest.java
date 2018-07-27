package org.qlrm.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.qlrm.test.JdbcBaseTest;
import org.qlrm.to.EmployeeTO;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class JdbcResultMapperTest extends JdbcBaseTest {

    private static final Logger LOGGER = LogManager.getLogger(JdbcResultMapperTest.class);

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
            LOGGER.debug(rec);
        }
    }

    @Test
    public void uniqueResult() throws SQLException {
        Statement stmt = con.createStatement();
        boolean ok = stmt.execute("SELECT ID, NAME FROM EMPLOYEE WHERE ID = 1");
        Assert.assertTrue(ok);

        EmployeeTO to = jdbcResultMapper.uniqueResult(stmt.getResultSet(), EmployeeTO.class);
        Assert.assertNotNull(to);

        LOGGER.debug(to);
    }
}
