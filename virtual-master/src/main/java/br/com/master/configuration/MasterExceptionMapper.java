package br.com.master.configuration;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.common.wrappers.CommonException;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-master <br>

 * @author macelai
 * @date: 18 de nov de 2018
 */
@ControllerAdvice
public class MasterExceptionMapper extends ResponseEntityExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
        String code = exception.getMessage();
        Object[] args = null;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (exception instanceof CommonException) {
            args = ((CommonException) exception).args();
            status = HttpStatus.valueOf(((CommonException) exception).status().getStatusCode());
        } else if (exception instanceof NoResultException) {
            code = "slave.obj.nao.localizado";
        }
        String message = messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        return handleExceptionInternal(exception, message, new HttpHeaders(), status, request);
    }

}
