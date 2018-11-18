package br.com.master.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;

import br.com.common.configuration.CommonConfiguration;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-master <br>

 * @author macelai
 * @date: 18 de nov de 2018
 */
@Configuration
public class MasterConfiguration extends CommonConfiguration {

    private static final String[] BASE_PACKAGES     = { "br.com.master" };
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
