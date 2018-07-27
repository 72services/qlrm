package org.qlrm.executor;

import org.qlrm.mapper.JpaResultMapper;
import org.qlrm.util.FileUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class JpaQueryExecutor {

    private final JpaResultMapper jpaResultMapper = new JpaResultMapper();

    public <T> List<T> executeSelect(EntityManager em, Class<T> clazz, String filename, Object... params) {
        String sqlString = FileUtil.getFileAsString(filename);
        Query query = em.createNativeQuery(sqlString);

        if (params.length > 0) {
            setParams(query, params);
        }

        return jpaResultMapper.list(query, clazz);
    }

    private void setParams(Query query, Object[] params) {
        int i = 1;
        for (Object param : params) {
            query.setParameter(i++, param);
        }
    }
}
