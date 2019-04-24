package br.com.master.configuration;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import br.com.master.wrappers.MasterJob;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 4 de abr de 2019
 */
@Configuration
public class QuartzConfiguration {

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

    public static final String  APPLICATION_CONTEXT      = "APPLICATION_CONTEXT";

    @Autowired
    private Environment         env;

    @Autowired
    private ApplicationContext  applicationContext;

    @Autowired
    private DataSource          dataSource;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor registry = new JobRegistryBeanPostProcessor();
        registry.setJobRegistry(jobRegistry);
        return registry;
    }

    @Bean
    public SchedulerFactoryBean quartzScheduler(Trigger[] triggers, JobDetail[] jobDetails) throws Exception {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setDataSource(dataSource);
        scheduler.setApplicationContextSchedulerContextKey(APPLICATION_CONTEXT);
        scheduler.setApplicationContext(applicationContext);
        scheduler.setQuartzProperties(quartzProperties());
        scheduler.setTriggers(triggers);
        scheduler.setJobDetails(jobDetails);
        return scheduler;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean properties = new PropertiesFactoryBean();
        properties.setLocation(new ClassPathResource("/config/quartz.properties"));
        properties.afterPropertiesSet();
        return properties.getObject();
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

    private Trigger triggerBuilder(String id, JobDetail job, String cron) {
        TriggerKey key = new TriggerKey(id, Scheduler.DEFAULT_GROUP);
        TriggerBuilder<Trigger> trigger = TriggerBuilder.newTrigger().withIdentity(key);
        trigger.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        trigger.startAt(new Date(2));
        trigger.forJob(job);
        return trigger.build();
    }

    private JobDetail jobBuilder(String id) {
        JobKey key = new JobKey(id, Scheduler.DEFAULT_GROUP);
        JobBuilder job = JobBuilder.newJob(MasterJob.class).withIdentity(key);
        // Set Job data map
        JobDataMap data = new JobDataMap();
        data.put("nome", id);
        job.setJobData(data);
        job.storeDurably();
        return job.build();
    }

}
