package br.com.master.configuration;

import javax.servlet.ServletContext;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import br.com.common.configuration.CommonConfiguration;
import br.com.master.business.impl.ConfiguracaoBusiness;
import br.com.master.domain.ConfiguracaoTO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 */
@Configuration
public class MasterConfiguration extends CommonConfiguration {

    private static final String[] BASE_PACKAGES           = { "br.com.master" };
    private static final String   MESSAGE_BASE_NAME       = "i18n/messages";
    private static final String   ARQUIVO_TAMANHO_BLOCO   = "arquivo.tamanho.bloco";
    private static final String   ARQUIVO_QTDE_REPLICACAO = "arquivo.qtde.replicacao";

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

    @Bean(name = "multipartResolver")
    CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(-1);
        return multipartResolver;
    }

    @Bean
    CommandLineRunner initializing(ConfiguracaoBusiness configuracaoBusiness, Environment env) {
        return args -> {
            long init = configuracaoBusiness.count();
            if (init == 0) {
                configuracaoPopulator(configuracaoBusiness, env);
            }
        };
    }

    @Bean(name = "masterBalance")
    MasterBalance masterBalance(Environment env, ServletContext context) {
        return new MasterBalance(env, context);
    }

    private void configuracaoPopulator(ConfiguracaoBusiness configuracaoBusiness, Environment env) {
        ConfiguracaoTO configuracao = new ConfiguracaoTO();
        configuracao.setTamanhoBloco(Integer.valueOf(env.getRequiredProperty(ARQUIVO_TAMANHO_BLOCO)));
        configuracao.setQtdeReplicacao(Integer.valueOf(env.getRequiredProperty(ARQUIVO_QTDE_REPLICACAO)));
        configuracaoBusiness.incluir(configuracao);
    }

}
