package br.com.slave.configuration;

import org.springframework.dao.DataAccessException;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 29 de out de 2018
 */
@SuppressWarnings("serial")
public class SlaveException extends DataAccessException {

    private Object[] args;

    public SlaveException(String message) {
        super(message);
    }

    public final void args(String... param) {
        args = param;
    }

    public Object[] args() {
        return args;
    }

}
