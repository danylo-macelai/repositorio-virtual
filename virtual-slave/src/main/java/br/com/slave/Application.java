package br.com.slave;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.wso2.msf4j.spring.MSF4JSpringApplication;
import org.wso2.msf4j.spring.SpringMicroservicesRunner;

import br.com.slave.configuration.CorsInterceptor;
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
        new MSF4JSpringApplication(Application.class);
        AnnotationConfigApplicationContext context = BeanUtils.instantiate(AnnotationConfigApplicationContext.class);
        context.register(SlaveConfiguration.class);
        context.refresh();

        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(context);
        classPathBeanDefinitionScanner.scan(BlocoResource.class.getPackage().getName());
        SpringMicroservicesRunner runner = context.getBean(SpringMicroservicesRunner.class);
        runner.addGlobalRequestInterceptor(context.getBean(CorsInterceptor.class));
        runner.deploy("/blocos", context.getBean(BlocoResource.class));
    }

}
