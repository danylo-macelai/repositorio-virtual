package br.com.master.configuration;

import br.com.common.configuration.CommonQuartzConfiguration;

import br.com.master.task.TaskGravacaoSlave;
import br.com.master.task.TaskInstanceSlave;
import br.com.master.task.TaskLimparDiretorio;
import br.com.master.task.TaskMigracaoSlave;
import br.com.master.task.TaskReplicacaoSlave;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 4 de abr de 2019
 * @version $
 */
@Configuration
public class MasterQuartzConfiguration extends CommonQuartzConfiguration {

    private static final String JOB_INSTANCE   = "jobInstance";
    private static final String JOB_GRAVACAO   = "JobGravacao";
    private static final String JOB_REPLICACAO = "jobReplicacao";
    private static final String JOB_LIMPAR     = "jobLimpar";
    private static final String JOB_MIGRACAO   = "jobMigracao";

    private static final String TRIGGER_INSTANCE         = "triggerInstance";
    private static final String TRIGGER_GRAVACAO         = "triggerGravacao";
    private static final String TRIGGER_REPLICACAO       = "triggerReplicacao";
    private static final String TRIGGER_LIMPAR_DIRETORIO = "triggerLimparDiretorio";
    private static final String TRIGGER_MIGRACAO         = "triggerMigracao";

    private static final String CRON_INSTANCE_SLAVE   = "cron.instance.slave";
    private static final String CRON_GRAVACAO_SLAVE   = "cron.gravacao.slave";
    private static final String CRON_REPLICACAO_SLAVE = "cron.replicacao.slave";
    private static final String CRON_MIGRACAO         = "cron.migracao.slave";
    private static final String CRON_LIMPAR_DIRETORIO = "cron.limpar.diretorio";

    @Bean(JOB_INSTANCE)
    public JobDetail jobInstance() {
        return jobBuilder(JOB_INSTANCE, TaskInstanceSlave.class);
    }

    @Bean(JOB_GRAVACAO)
    public JobDetail jobGravacao() {
        return jobBuilder(JOB_GRAVACAO, TaskGravacaoSlave.class);
    }

    @Bean(JOB_REPLICACAO)
    public JobDetail jobReplicacao() {
        return jobBuilder(JOB_REPLICACAO, TaskReplicacaoSlave.class);
    }

    @Bean(JOB_LIMPAR)
    public JobDetail jobLimpar() {
        return jobBuilder(JOB_LIMPAR, TaskLimparDiretorio.class);
    }

    @Bean(JOB_MIGRACAO)
    public JobDetail jobMigracao() {
        return jobBuilder(JOB_MIGRACAO, TaskMigracaoSlave.class);
    }

    @Bean(TRIGGER_GRAVACAO)
    public Trigger triggerGravacao(@Qualifier(JOB_GRAVACAO) JobDetail job) {
        return triggerBuilder(TRIGGER_GRAVACAO, job, env.getRequiredProperty(CRON_GRAVACAO_SLAVE));
    }

    @Bean(TRIGGER_INSTANCE)
    public Trigger triggerInstance(@Qualifier(JOB_INSTANCE) JobDetail job) {
        return triggerBuilder(TRIGGER_INSTANCE, job, env.getRequiredProperty(CRON_INSTANCE_SLAVE));
    }

    @Bean(TRIGGER_REPLICACAO)
    public Trigger triggerReplicacao(@Qualifier(JOB_REPLICACAO) JobDetail job) {
        return triggerBuilder(TRIGGER_REPLICACAO, job, env.getRequiredProperty(CRON_REPLICACAO_SLAVE));
    }

    @Bean(TRIGGER_LIMPAR_DIRETORIO)
    public Trigger triggerLimparDiretorio(@Qualifier(JOB_LIMPAR) JobDetail job) {
        return triggerBuilder(TRIGGER_LIMPAR_DIRETORIO, job, env.getRequiredProperty(CRON_LIMPAR_DIRETORIO));
    }

    @Bean(TRIGGER_MIGRACAO)
    public Trigger triggerMigracao(@Qualifier(JOB_MIGRACAO) JobDetail job) {
        return triggerBuilder(TRIGGER_MIGRACAO, job, env.getRequiredProperty(CRON_MIGRACAO));
    }

}
