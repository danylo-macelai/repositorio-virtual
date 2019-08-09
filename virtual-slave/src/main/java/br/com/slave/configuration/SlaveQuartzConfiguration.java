package br.com.slave.configuration;

import br.com.common.configuration.CommonQuartzConfiguration;

import br.com.slave.task.TaskContagem;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    private static final String JOB_CONTAGEM = "jobContagem";

    private static final String TRIGGER_CONTAGEM = "triggerContagem";

    private static final String CRON_CONTAGEM_VOLUME = "cron.contagem.volume";

    @Bean(JOB_CONTAGEM)
    public JobDetail jobContagem() {
        return jobBuilder(JOB_CONTAGEM, TaskContagem.class);
    }

    @Bean(TRIGGER_CONTAGEM)
    public Trigger triggerContagem(@Qualifier(JOB_CONTAGEM) JobDetail job) {
        return triggerBuilder(TRIGGER_CONTAGEM, job, env.getRequiredProperty(CRON_CONTAGEM_VOLUME));
    }

}
