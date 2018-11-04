package br.com.common.configuration;

import javax.ws.rs.core.Response.Status;

import org.springframework.dao.DataAccessException;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 4 de nov de 2018
 */
@SuppressWarnings("serial")
public class CommonException extends DataAccessException {

    private Status   statusType = Status.BAD_REQUEST;
    private Object[] args;

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public final CommonException status(Status status) {
        statusType = status;
        return this;
    }

    public final CommonException args(String... param) {
        args = param;
        return this;
    }

    public Object[] args() {
        return args;
    }

    public Status status() {
        return statusType;
    }

}
