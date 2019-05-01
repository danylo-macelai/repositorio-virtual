package br.com.slave.configuration;

import br.com.common.configuration.CommonQuartzConfiguration;

import br.com.slave.task.TaskContagem;

import java.sql.Connection;
import java.sql.Statement;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de abr de 2019
 * @version $
 */
@Configuration
public class SlaveQuartzConfiguration extends CommonQuartzConfiguration {

    public static final String  JOB_CONTAGEM         = "jobContagem";
    public static final String  TRIGGER_CONTAGEM     = "triggerContagem";
    private static final String CRON_CONTAGEM_VOLUME = "cron.contagem.volume";
    private static final String JOB_STORE_PLATFORM   = "job.store.platform";

    @Autowired
    ResourceLoader resourceLoader;

    @Configuration
    class InitializingQuartzDataSource {
        public InitializingQuartzDataSource() {
            try {
                final Connection connection = DataSourceUtils.getConnection(dataSource);
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("SELECT * FROM qrtz_job_details");
                } catch (final Exception e) {
                    final String platform = env.getProperty(JOB_STORE_PLATFORM);
                    final String schemaLocation = "classpath:org/quartz/impl/jdbcjobstore/tables_" + platform + ".sql";
                    final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                    populator.addScript(resourceLoader.getResource(schemaLocation));
                    populator.setContinueOnError(true);
                    populator.setCommentPrefix("--");
                    populator.populate(connection);
                } finally {
                    DataSourceUtils.releaseConnection(connection, dataSource);
                }
            } catch (final Throwable ex) {
                throw new SlaveException("Failed to execute database script", ex);
            }
        }
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        final SpringBeanJobFactory springBeanJobFactory = new SpringBeanJobFactory();
        springBeanJobFactory.setApplicationContext(applicationContext);
        return springBeanJobFactory;
    }

    @Bean(JOB_CONTAGEM)
    public JobDetail jobContagem() {
        return jobBuilder(JOB_CONTAGEM, TaskContagem.class);
    }

    @Bean(TRIGGER_CONTAGEM)
    public Trigger triggerContagem(@Qualifier(JOB_CONTAGEM) JobDetail job) {
        return triggerBuilder(TRIGGER_CONTAGEM, job, env.getRequiredProperty(CRON_CONTAGEM_VOLUME));
    }

}