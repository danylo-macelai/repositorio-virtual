package br.com.common.utils;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.stream.Stream;

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

    /**
     * Cria um Array concatenado com os elementos do one seguido por todos os elementos two.
     *
     * @param one
     * @param two
     * @return T[]
     */
    public static String[] concat(String[] one, String[] two) {
        return Stream.concat(Arrays.stream(one), Arrays.stream(two)).toArray(String[]::new);
    }

}
