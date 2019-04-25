package br.com.slave.configuration;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import br.com.common.configuration.CommonQuartzConfiguration;
import br.com.slave.task.TaskContagem;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de abr de 2019
 */
@Configuration
public class SlaveQuartzConfiguration extends CommonQuartzConfiguration {
    
    public static final String  JOB_CONTAGEM         = "jobContagem";
    
    public static final String  TRIGGER_CONTAGEM     = "triggerContagem";
    
    private static final String CRON_CONTAGEM_VOLUME = "cron.contagem.volume";
    
    @Autowired
    private Environment         env;
    
    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        SpringBeanJobFactory springBeanJobFactory = new SpringBeanJobFactory();
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
