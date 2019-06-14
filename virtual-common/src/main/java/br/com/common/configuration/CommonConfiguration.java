package br.com.common.configuration;

import static br.com.common.utils.Utils.concat;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 29 de abr de 2019
 * @version $
 */
@EnableTransactionManagement
public abstract class CommonConfiguration {

    private static final String[] PACKAGES = { "br.com.common" };

    private static final String DB_DRIVER_CLASS     = "db.driver";
    private static final String DB_PASSWORD         = "db.password";
    private static final String DB_URL              = "db.url";
    private static final String DB_USER             = "db.username";
    private static final String DB_VALIDATION_QUERY = "db.validation.query";

    private static final String HIBERNATE_DIALECT         = "hibernate.dialect";
    private static final String HIBERNATE_FORMAT_SQL      = "hibernate.format_sql";
    private static final String HIBERNATE_HBM2DDL_AUTO    = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";
    private static final String HIBERNATE_SHOW_SQL        = "hibernate.show_sql";
    private static final String HIBERNATE_LOB_CREATION    = "hibernate.jdbc.lob.non_contextual_creation";

    protected abstract String[] packagesToScan();

    @Bean(destroyMethod = "close")
    DataSource dataSource(Environment env) {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername(env.getRequiredProperty(DB_USER));
        dataSource.setPassword(env.getRequiredProperty(DB_PASSWORD));
        dataSource.setUrl(env.getRequiredProperty(DB_URL));
        dataSource.setDriverClassName(env.getRequiredProperty(DB_DRIVER_CLASS));

        dataSource.setInitialSize(20);
        dataSource.setMaxActive(20);
        dataSource.setMaxIdle(8);
        dataSource.setMinIdle(4);
        dataSource.setMaxWait(30000);

        dataSource.setValidationQuery(env.getProperty(DB_VALIDATION_QUERY));
        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(5000);
        dataSource.setMinEvictableIdleTimeMillis(4000);
        return dataSource;
    }

    @Bean
    PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env) {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan(concat(packagesToScan(), PACKAGES));

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        final Properties jpaProperties = new Properties();
        jpaProperties.put(HIBERNATE_DIALECT, env.getRequiredProperty(HIBERNATE_DIALECT));
        jpaProperties.put(HIBERNATE_HBM2DDL_AUTO, env.getRequiredProperty(HIBERNATE_HBM2DDL_AUTO));
        jpaProperties.put(HIBERNATE_NAMING_STRATEGY, env.getRequiredProperty(HIBERNATE_NAMING_STRATEGY));
        jpaProperties.put(HIBERNATE_SHOW_SQL, env.getRequiredProperty(HIBERNATE_SHOW_SQL));
        jpaProperties.put(HIBERNATE_FORMAT_SQL, env.getRequiredProperty(HIBERNATE_FORMAT_SQL));
        jpaProperties.put(HIBERNATE_LOB_CREATION, env.getRequiredProperty(HIBERNATE_LOB_CREATION));
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    /**
     * Crie uma nova transaction com o dado comportamento de propagação, suspendendo a transação atual, se houver.
     *
     * @return TransactionDefinition
     * @throws DataAccessException
     */
    @Bean
    TransactionTemplate transactionTemplate(JpaTransactionManager transactionManager) throws DataAccessException {
        final TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        template.setTimeout(300);
        return template;
    }

}
