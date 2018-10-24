package br.com.slave;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.wso2.msf4j.spring.MSF4JSpringApplication;

import br.com.slave.configuration.SlaveConfiguration;
import br.com.slave.resource.BlocoResource;

/**
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 23 de out de 2018
 */
public class Application {

    public static void main(String[] args) {
        MSF4JSpringApplication application = new MSF4JSpringApplication(Application.class);
        AnnotationConfigApplicationContext context = BeanUtils.instantiate(AnnotationConfigApplicationContext.class);
        context.register(SlaveConfiguration.class);
        context.refresh();
        application.addService(context, BlocoResource.class, "/blocos");

    }

}
