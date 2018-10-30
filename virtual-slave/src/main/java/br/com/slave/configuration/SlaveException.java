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

    private final String message;
    private Object[]     args;

    public SlaveException(String message) {
        super(message);
        this.message = message;
    }

    public final void args(String... param) {
        args = param;
    }

    public String code() {
        return message;
    }

    public Object[] args() {
        return args;
    }

}
