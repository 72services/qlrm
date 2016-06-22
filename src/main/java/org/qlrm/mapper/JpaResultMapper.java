package org.qlrm.mapper;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

public class JpaResultMapper extends ResultMapper {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_BOX_TYPE_MAP = new HashMap<>();

    {
        PRIMITIVE_TO_BOX_TYPE_MAP.put(int.class, Integer.class);
        PRIMITIVE_TO_BOX_TYPE_MAP.put(long.class, Long.class);
        PRIMITIVE_TO_BOX_TYPE_MAP.put(byte.class, Byte.class);
        PRIMITIVE_TO_BOX_TYPE_MAP.put(boolean.class, Boolean.class);
        PRIMITIVE_TO_BOX_TYPE_MAP.put(char.class, Character.class);
        PRIMITIVE_TO_BOX_TYPE_MAP.put(float.class, Float.class);
        PRIMITIVE_TO_BOX_TYPE_MAP.put(double.class, Double.class);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Query q, Class<T> clazz)
            throws IllegalArgumentException {
        List<T> result = new ArrayList<T>();

        List<Object[]> list = postProcessResultList(q.getResultList());

        Constructor<?> ctor = null;
        if (list != null && !list.isEmpty()) {
            ctor = findConstructor(clazz, list.get(0));
        }
        for (Object[] obj : list) {
            result.add((T) createInstance(ctor, obj));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> postProcessResultList(List<?> rawResults) {
        List<Object[]> result = new ArrayList<>();

        if (rawResults.size() == 1) {
            for (Object rawResult : rawResults) {
                result.add(postProcessSingleResult(rawResult));
            }
        } else {
            result = (List<Object[]>) rawResults;
        }

        return result;
    }

    private Object[] postProcessSingleResult(Object rawResult) {
        return rawResult instanceof Object[] ? (Object[]) rawResult
                : new Object[]{rawResult};
    }

    public <T> T uniqueResult(Query q, Class<T> clazz) {
        Object[] rec = postProcessSingleResult(q.getSingleResult());
        Constructor<?> ctor = findConstructor(clazz, rec);

        return createInstance(ctor, rec);
    }

    private Constructor<?> findConstructor(Class<?> clazz, Object... args) {
        Constructor<?> result = null;
        final Constructor<?>[] ctors = clazz.getDeclaredConstructors();

        // More stable check
        if (ctors.length == 1
                && ctors[0].getParameterTypes().length == args.length) {
            // INFO stefanheimberg: wenn nur ein konstruktor, dann diesen
            // verwenden
            result = ctors[0];
        }
        if (ctors.length > 1) {
            // INFO stefanheimberg: wenn mehrere konstruktor, dann den mit der
            // korrekten signatur verwenden

            NEXT_CONSTRUCTOR:
            for (Constructor<?> ctor : ctors) {
                final Class<?>[] parameterTypes = postProcessConstructorParameterTypes(ctor
                        .getParameterTypes());
                if (parameterTypes.length != args.length) {
                    // INFO stefanheimberg: anzahl parameter stimmt nicht
                    continue NEXT_CONSTRUCTOR;
                }
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (args[i] != null) {
                        Class<?> argType = convertToBoxTypeIfPrimitive(args[i]
                                .getClass());
                        if (!parameterTypes[i].isAssignableFrom(argType)) {
                            continue NEXT_CONSTRUCTOR;
                        }
                    }
                }
                result = ctor;
                break;
            }
        }
        if (null == result) {
            StringBuilder sb = new StringBuilder("No constructor taking:\n");
            for (Object object : args) {
                if (object != null) {
                    sb.append("\t").append(object.getClass().getName())
                            .append("\n");
                }
            }
            throw new RuntimeException(sb.toString());
        }
        return result;
    }

    /**
     * <p>
     * According to the JLS primitive types are not assignable to their box type
     * counterparts. E. g. int.class.isAssignableFrom(Integer.class) returns
     * false.
     * </p>
     * <p>
     * In order to make the isAssignable check in findConstructors work with
     * primitives, the check uses this method to convert possible primitive
     * constructor argument types to their box type counterparts.
     * </p>
     */
    private Class<?>[] postProcessConstructorParameterTypes(
            Class<?>[] rawParameterTypes) {
        Class<?>[] result = new Class<?>[rawParameterTypes.length];
        for (int i = 0; i < rawParameterTypes.length; i++) {
            Class<?> currentType = rawParameterTypes[i];
            result[i] = convertToBoxTypeIfPrimitive(currentType);
        }

        return result;
    }

    /**
     * @return The box type matching the provided primitive type or
     * <code>primitiveType</code> if no match could be found (e. g. the provided
     * value was not a primitive type).
     */
    private Class<?> convertToBoxTypeIfPrimitive(Class<?> primitiveType) {
        if (PRIMITIVE_TO_BOX_TYPE_MAP.containsKey(primitiveType)) {
            return PRIMITIVE_TO_BOX_TYPE_MAP.get(primitiveType);
        } else {
            return primitiveType;
        }
    }
}
