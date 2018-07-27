package org.qlrm.executor;

import org.qlrm.mapper.JdbcResultMapper;
import org.qlrm.util.FileUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcQueryExecutor {

    private final JdbcResultMapper jdbcResultMapper = new JdbcResultMapper();

    public <T> List<T> executeSelect(Connection connection, Class<T> clazz, String filename, Object... params) {
        try {
            String sqlString = FileUtil.getFileAsString(filename);
            PreparedStatement prepareStatement = connection.prepareStatement(sqlString);

            if (params.length > 0) {
                setParams(prepareStatement, params);
            }

            ResultSet resultSet = prepareStatement.executeQuery();

            return jdbcResultMapper.list(resultSet, clazz);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void setParams(PreparedStatement prepareStatement, Object[] params) throws SQLException {
        int i = 1;
        for (Object param : params) {
            prepareStatement.setObject(i++, param);
        }
    }
}
