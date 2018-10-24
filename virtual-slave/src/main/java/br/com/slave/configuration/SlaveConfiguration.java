package br.com.slave.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.wso2.msf4j.spring.MSF4JSpringConfiguration;

import br.com.common.configuration.CommonConfiguration;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de out de 2018
 */
@Configuration
@ComponentScans(value = {
        @ComponentScan("br.com.slave"),
        @ComponentScan("org.wso2.msf4j.spring")
})
@Import({ MSF4JSpringConfiguration.class })
public class SlaveConfiguration extends CommonConfiguration {

    private static final String[] BASE_PACKAGES     = { "br.com.slave"  };
    private static final String   MESSAGE_BASE_NAME = "i18n/messages";

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] packagesToScan() {
        return BASE_PACKAGES;
    }

    @Configuration
    @PropertySource("file:///${configuracoes.path}")
    static class ApplicationProperties {

    }

    @Bean
    MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MESSAGE_BASE_NAME);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
