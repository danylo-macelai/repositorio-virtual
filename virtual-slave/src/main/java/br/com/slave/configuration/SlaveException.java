package br.com.slave.configuration;

import br.com.common.wrappers.CommonException;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 29 de out de 2018
 * @version $
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