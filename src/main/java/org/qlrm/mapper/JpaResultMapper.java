package org.qlrm.mapper;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class JpaResultMapper extends ResultMapper {

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Query q, Class<T> clazz) throws IllegalArgumentException {
        List<T> result = new ArrayList<T>();
        Constructor<?> ctor = (Constructor<?>) clazz.getDeclaredConstructors()[0];
        List<Object[]> list = q.getResultList();
        for (Object obj : list) {
            if (ctor.getParameterTypes().length == 1) {
                obj = new Object[]{obj};
            }
            result.add((T) createInstance(ctor, (Object[]) obj));
        }
        return result;
    }

    public <T> T uniqueResult(Query q, Class<T> clazz) {
        Object rec = q.getSingleResult();
        Constructor<?> ctor = (Constructor<?>) clazz.getDeclaredConstructors()[0];
        if (ctor.getParameterTypes().length == 1) {
            rec = new Object[]{rec};
        }
        return createInstance(ctor, (Object[]) rec);
    }

}
