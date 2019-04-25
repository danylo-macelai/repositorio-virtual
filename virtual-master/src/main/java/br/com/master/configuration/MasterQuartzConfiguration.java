package br.com.master.configuration;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import br.com.common.configuration.CommonQuartzConfiguration;
import br.com.master.wrappers.MasterJob;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 4 de abr de 2019
 */
@Configuration
public class MasterQuartzConfiguration extends CommonQuartzConfiguration {
    
    public static final String  JOB_INSTANCE             = "jobInstance";
    public static final String  JOB_GRAVACAO             = "JobGravacao";
    public static final String  JOB_REPLICACAO           = "jobReplicacao";
    public static final String  JOB_LIMPAR               = "jobLimpar";
    public static final String  JOB_MIGRACAO             = "jobMigracao";
    
    public static final String  TRIGGER_INSTANCE         = "triggerInstance";
    public static final String  TRIGGER_GRAVACAO         = "triggerGravacao";
    public static final String  TRIGGER_REPLICACAO       = "triggerReplicacao";
    public static final String  TRIGGER_LIMPAR_DIRETORIO = "triggerLimparDiretorio";
    public static final String  TRIGGER_MIGRACAO         = "triggerMigracao";
    
    private static final String CRON_INSTANCE_SLAVE      = "cron.instance.slave";
    private static final String CRON_GRAVACAO_SLAVE      = "cron.gravacao.slave";
    private static final String CRON_REPLICACAO_SLAVE    = "cron.replicacao.slave";
    private static final String CRON_MIGRACAO            = "cron.migracao.slave";
    private static final String CRON_LIMPAR_DIRETORIO    = "cron.limpar.diretorio";
    
    @Autowired
    private Environment         env;
    
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor registry = new JobRegistryBeanPostProcessor();
        registry.setJobRegistry(jobRegistry);
        return registry;
    }
    
    @Bean(JOB_INSTANCE)
    public JobDetail jobInstance() {
        return jobBuilder(JOB_INSTANCE);
    }
    
    @Bean(JOB_GRAVACAO)
    public JobDetail jobGravacao() {
        return jobBuilder(JOB_GRAVACAO);
    }
    
    @Bean(JOB_REPLICACAO)
    public JobDetail jobReplicacao() {
        return jobBuilder(JOB_REPLICACAO);
    }
    
    @Bean(JOB_LIMPAR)
    public JobDetail jobLimpar() {
        return jobBuilder(JOB_LIMPAR);
    }
    
    @Bean(JOB_MIGRACAO)
    public JobDetail jobMigracao() {
        return jobBuilder(JOB_MIGRACAO);
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
    
    private JobDetail jobBuilder(String id) {
        return super.jobBuilder(id, MasterJob.class);
    }
    
}
