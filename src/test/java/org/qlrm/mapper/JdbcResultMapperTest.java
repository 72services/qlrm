package org.qlrm.mapper;

import org.junit.jupiter.api.Test;
import org.qlrm.test.JdbcBaseTest;
import org.qlrm.to.EmployeeTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcResultMapperTest extends JdbcBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcResultMapperTest.class);

    private static final JdbcResultMapper jdbcResultMapper = new JdbcResultMapper();

    @Test
    void testSql() throws SQLException {
        Statement stmt = con.createStatement();
        boolean ok = stmt.execute("SELECT ID, NAME FROM EMPLOYEE");
        assertTrue(ok);

        List<EmployeeTO> list = jdbcResultMapper.list(stmt.getResultSet(), EmployeeTO.class);
        assertNotNull(list);
        assertFalse(list.isEmpty());

        LOGGER.debug(list.toString());
    }

    @Test
    void uniqueResult() throws SQLException {
        Statement stmt = con.createStatement();
        boolean ok = stmt.execute("SELECT ID, NAME FROM EMPLOYEE WHERE ID = 1");
        assertTrue(ok);

        EmployeeTO to = jdbcResultMapper.uniqueResult(stmt.getResultSet(), EmployeeTO.class);
        assertNotNull(to);

        LOGGER.debug(to.toString());
    }
}
