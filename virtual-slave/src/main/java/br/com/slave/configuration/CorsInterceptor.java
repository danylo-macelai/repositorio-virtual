package br.com.slave.configuration;

import org.springframework.stereotype.Component;
import org.wso2.msf4j.Request;
import org.wso2.msf4j.Response;
import org.wso2.msf4j.interceptor.RequestInterceptor;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 29 de out de 2018
 * @version $
 */
@Component
public class CorsInterceptor implements RequestInterceptor {

    @Override
    public boolean interceptRequest(Request request, Response response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

        if (request.getHeader("Origin") != null && !request.getHeader("Origin").isEmpty()) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
        return true;
    }

}
