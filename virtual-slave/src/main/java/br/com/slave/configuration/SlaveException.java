package br.com.slave.configuration;

import br.com.common.configuration.CommonException;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 29 de out de 2018
 */
@SuppressWarnings("serial")
public class SlaveException extends CommonException {

    public SlaveException(String message) {
        super(message);
    }

    public SlaveException(String message, Throwable cause) {
        super(message, cause);
    }

}