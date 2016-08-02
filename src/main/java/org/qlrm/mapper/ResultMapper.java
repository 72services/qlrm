package org.qlrm.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ResultMapper {

    @SuppressWarnings(value = "unchecked")
    protected <T> T createInstance(Constructor<?> ctor, Object[] args) {
        try {
            return (T) ctor.newInstance(args);
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder("no constructor taking:\n");
            for (Object object : args) {
                if (object != null) {
                    sb.append("\t").append(object.getClass().getName()).append("\n");
                }
            }
            throw new RuntimeException(sb.toString(), e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

}
