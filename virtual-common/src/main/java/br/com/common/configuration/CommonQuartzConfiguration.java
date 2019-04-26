package br.com.common.configuration;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import br.com.common.wrappers.CommonJob;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 24 de abr de 2019
 */
public abstract class CommonQuartzConfiguration {

    public static final String   APPLICATION_CONTEXT = "APPLICATION_CONTEXT";

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected DataSource         dataSource;

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

    protected final Trigger triggerBuilder(String id, JobDetail job, String cron) {
        TriggerKey key = new TriggerKey(id, Scheduler.DEFAULT_GROUP);
        TriggerBuilder<Trigger> trigger = TriggerBuilder.newTrigger().withIdentity(key);
        trigger.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        trigger.startAt(new Date(2));
        trigger.forJob(job);
        return trigger.build();
    }

    protected final <C extends CommonJob> JobDetail jobBuilder(String id, Class<C> task) {
        JobKey key = new JobKey(id, Scheduler.DEFAULT_GROUP);
        JobBuilder job = JobBuilder.newJob(task).withIdentity(key);
        // Set Job data map
        JobDataMap data = new JobDataMap();
        data.put("nome", id);
        job.setJobData(data);
        job.storeDurably();
        return job.build();
    }

}
