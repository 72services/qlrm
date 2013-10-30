package org.qlrm.mapper;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcResultMapper extends ResultMapper {

    @SuppressWarnings("unchecked")
    public <T> List<T> list(ResultSet rs, Class<T> clazz) throws SQLException {
        List<T> result = new ArrayList<T>();
        Constructor<?> ctor = getConstructor(rs, clazz);

        while (rs.next()) {
            Object[] objs = new Object[ctor.getParameterTypes().length];
            for (int i = 0; i < ctor.getParameterTypes().length; i++) {
                objs[i] = rs.getObject(i + 1);
            }
            result.add((T) createInstance(ctor, objs));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T uniqueResult(ResultSet rs, Class<T> clazz) throws SQLException {
        Constructor<T> ctor = (Constructor<T>) getConstructor(rs, clazz);
        rs.next();
        Object[] objs = new Object[ctor.getParameterTypes().length];
        for (int i = 0; i < ctor.getParameterTypes().length; i++) {
            objs[i] = rs.getObject(i + 1);
        }
        return createInstance(ctor, objs);
    }

    private Constructor<?> getConstructor(ResultSet rs, Class<?> clazz) throws SQLException {
        Constructor<?> ctor = null;
        outer: for (Constructor<?> ct : clazz.getDeclaredConstructors()) {
            if (ct.getParameterTypes().length == rs.getMetaData().getColumnCount()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (!ct.getParameterTypes()[i - 1].getName().equals(
                            rs.getMetaData().getColumnClassName(i))) {
                        continue outer;
                    }
                }
                ctor = ct;
                break;
            }
        }
        if (ctor == null) {
            StringBuilder sb = new StringBuilder("no constructor taking: ");
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                sb.append(rs.getMetaData().getColumnClassName(i)).append(", ");
            }
            throw new RuntimeException(sb.toString());
        }
        return ctor;
    }
}
