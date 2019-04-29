package br.com.master.configuration;

import br.com.common.wrappers.CommonException;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 * @version $
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