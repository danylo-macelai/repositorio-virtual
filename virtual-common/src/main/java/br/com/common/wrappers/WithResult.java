package br.com.common.wrappers;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 22 de abr de 2019
 */
@FunctionalInterface
public interface WithResult<T> {

    T get();

}
