package br.com.common.utils;

import java.lang.reflect.ParameterizedType;

/**
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 22 de out de 2018
 */
public abstract class Utils {

    /**
     * O método {@link Utils#actualType(Object)} retorna o tipo parametrizado da classe genérica.
     *
     * @param object
     * @return Class<T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> Class<T> actualType(Object object) {
        ParameterizedType thisType = (ParameterizedType) object.getClass().getGenericSuperclass();
        return (Class<T>) thisType.getActualTypeArguments()[0];
    }

}
