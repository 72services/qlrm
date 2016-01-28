package org.qlrm.mapper;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class JpaResultMapper extends ResultMapper {

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Query q,
            Class<T> clazz) throws IllegalArgumentException {
        List<T> result = new ArrayList<T>();
		List<Object[]> list = postProcessResultList(q.getResultList());

        Constructor<?> ctor = null;
        // why finding a constructor for each item? The result set is the same!
        if (list != null && !list.isEmpty()) {
			ctor = findConstructor(clazz, list.get(0));
        }
		for (Object[] obj : list) {
			result.add((T) createInstance(ctor, obj));
        }
        return result;
    }

	private List<Object[]> postProcessResultList(List<?> rawResults) {
		List<Object[]> result = new ArrayList<>();
		for (Object rawResult : rawResults) {
			result.add(postProcessSingleResult(rawResult));
		}

		return result;
	}

	private Object[] postProcessSingleResult(Object rawResult) {
		return rawResult instanceof Object[] ? (Object[]) rawResult : new Object[] { rawResult };
	}

    public <T> T uniqueResult(Query q,
            Class<T> clazz) {
		Object[] rec = postProcessSingleResult(q.getSingleResult());
		Constructor<?> ctor = findConstructor(clazz, rec);

		return createInstance(ctor, rec);
    }

    private Constructor<?> findConstructor(Class<?> clazz,
 Object... args) {
        Constructor<?> ctor = null;
        final Constructor<?>[] ctors = clazz.getDeclaredConstructors();

        // More stable check
        if (ctors.length == 1 && ctors[0].getParameterTypes().length == args.length) {
            // INFO stefanheimberg: wenn nur ein konstruktor, dann diesen verwenden
            ctor = ctors[0];
        }
        if (ctors.length > 1) {
            // INFO stefanheimberg: wenn mehrere konstruktor, dann den mit der korrekten signatur verwenden
            for (Constructor<?> ctor2 : ctors) {
                final Class<?>[] parameterTypes = ctor2.getParameterTypes();
                if (parameterTypes.length != args.length) {
                    // INFO stefanheimberg: anzahl parameter stimmt nicht
                    continue;
                }
                boolean signatureCheckFailed = false;
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (args[i] != null && !parameterTypes[i].isAssignableFrom(args[i].getClass())) {
                        signatureCheckFailed = false;
                        break;
                    }
                }
                if (!signatureCheckFailed) {
                    ctor = ctor2;
                    break;
                }
            }
        }
        if (null == ctor) {
            StringBuilder sb = new StringBuilder("No constructor taking:\n");
            for (Object object : args) {
                sb.append("\t").append(object.getClass().getName()).append("\n");
            }
            throw new RuntimeException(sb.toString());
        }
        return ctor;
    }

}
