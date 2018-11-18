package br.com.master.configuration;

import br.com.common.configuration.CommonException;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-master <br>

 * @author macelai
 * @date: 18 de nov de 2018
 */
@SuppressWarnings("serial")
public class MasterException extends CommonException {

    public MasterException(String message) {
        super(message);
    }

    public MasterException(String message, Throwable cause) {
        super(message, cause);
    }

}