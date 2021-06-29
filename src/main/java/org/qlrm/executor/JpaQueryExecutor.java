package org.qlrm.executor;

import org.qlrm.mapper.JpaResultMapper;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class JpaQueryExecutor {

    private final JpaResultMapper jpaResultMapper = new JpaResultMapper();

    /**
     * Executes an SQL select from a file an returns objects of the requested class
     *
     * @param em          {@link javax.persistence.EntityManager}
     * @param clazz       Type to return
     * @param filename    File containing the SQL select
     * @param pageRequest {@link PageRequest}
     * @param params      List of parameters
     * @param <T>         Type
     * @return List of objects
     */
    public <T> List<T> executeSelect(EntityManager em, Class<T> clazz, String filename, PageRequest pageRequest, Object... params) {
        String sqlString = FileUtil.getFileAsString(filename);
        Query query = em.createNativeQuery(sqlString);

        if (pageRequest.getFirstResult() != null) {
            query.setFirstResult(pageRequest.getFirstResult());
        }
        if (pageRequest.getMaxResult() != null) {
            query.setMaxResults(pageRequest.getMaxResult());
        }

        if (params.length > 0) {
            setParams(query, params);
        }

        return jpaResultMapper.list(query, clazz);
    }

    /**
     * Executes an SQL select from a file an returns objects of the requested class
     *
     * @param em       {@link javax.persistence.EntityManager}
     * @param clazz    Type to return
     * @param filename File containing the SQL select
     * @param params   List of parameters
     * @param <T>      Type
     * @return List of objects
     */
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
