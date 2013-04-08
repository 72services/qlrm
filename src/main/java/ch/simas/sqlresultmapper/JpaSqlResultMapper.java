package ch.simas.sqlresultmapper;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class JpaSqlResultMapper {

    public static <T> List<T> list(Query q, Class<T> clazz) throws IllegalArgumentException {
        List<T> result = new ArrayList<T>();
        Constructor<?> ctor = (Constructor<?>) clazz.getDeclaredConstructors()[0];
        List<Object[]> list = q.getResultList();
        for (Object obj : list) {
            if (ctor.getParameterTypes().length == 1) {
                obj = new Object[]{obj};
            }
            result.add((T) createBean(ctor, (Object[]) obj));
        }
        return result;
    }

    public static <T> T uniqueResult(Query q, Class<T> clazz) {
        Object rec = q.getSingleResult();
        Constructor<?> ctor = (Constructor<?>) clazz.getDeclaredConstructors()[0];
        if (ctor.getParameterTypes().length == 1) {
            rec = new Object[]{rec};
        }
        return createBean(ctor, (Object[]) rec);
    }

    private static <T> T createBean(Constructor<?> ctor, Object[] args) {
        try {
            return (T) ctor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
