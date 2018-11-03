package br.com.slave.configuration;

import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 29 de out de 2018
 */
@Component
public class SlaveExceptionMapper implements ExceptionMapper<Exception> {
    
    @Autowired
    MessageSource messageSource;
    
    @Override
    public Response toResponse(Exception exception) {
        String code = exception.getMessage();
        Object[] args = null;
        if (exception instanceof SlaveException) {
            args = ((SlaveException) exception).args();
        } else if (exception instanceof NoResultException) {
            code = "no.result.exception";
        }
        String message = messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        return Response.status(Response.Status.BAD_REQUEST).entity(message).type("text/plain").build();
    }
}
