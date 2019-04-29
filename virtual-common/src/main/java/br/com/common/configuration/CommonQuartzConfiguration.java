package br.com.common.configuration;

import br.com.common.wrappers.CommonJob;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 24 de abr de 2019
 * @version $
 */
public abstract class CommonQuartzConfiguration {

    public static final String  APPLICATION_CONTEXT       = "APPLICATION_CONTEXT";
    private static final String JOB_DRIVER_DELEGATE_CLASS = "org.quartz.jobStore.driverDelegateClass";

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected Environment env;

    @Bean
    public SchedulerFactoryBean quartzScheduler(Trigger[] triggers, JobDetail[] jobDetails) throws Exception {
        final SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
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
        final PropertiesFactoryBean factory = new PropertiesFactoryBean();
        factory.setLocation(new ClassPathResource("/config/quartz.properties"));
        factory.afterPropertiesSet();

        final Properties properties = factory.getObject();
        properties.put(JOB_DRIVER_DELEGATE_CLASS, env.getProperty(JOB_DRIVER_DELEGATE_CLASS));
        return properties;
    }

    protected final Trigger triggerBuilder(String id, JobDetail job, String cron) {
        final TriggerKey key = new TriggerKey(id, Scheduler.DEFAULT_GROUP);
        final TriggerBuilder<Trigger> trigger = TriggerBuilder.newTrigger().withIdentity(key);
        trigger.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        trigger.startAt(new Date(2));
        trigger.forJob(job);
        return trigger.build();
    }

    protected final <C extends CommonJob> JobDetail jobBuilder(String id, Class<C> task) {
        final JobKey key = new JobKey(id, Scheduler.DEFAULT_GROUP);
        final JobBuilder job = JobBuilder.newJob(task).withIdentity(key);
        // Set Job data map
        final JobDataMap data = new JobDataMap();
        data.put("nome", id);
        job.setJobData(data);
        job.storeDurably();
        return job.build();
    }

}
