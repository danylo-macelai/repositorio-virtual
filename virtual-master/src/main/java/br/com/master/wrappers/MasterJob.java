package br.com.master.wrappers;

import static br.com.common.configuration.CommonQuartzConfiguration.APPLICATION_CONTEXT;

import java.io.Serializable;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;

import br.com.common.wrappers.CommonJob;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 4 de abr de 2019
 */
@SuppressWarnings("serial")
public class MasterJob extends CommonJob implements Serializable {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext().get(APPLICATION_CONTEXT);
            JobLocator jobLocator = applicationContext.getBean(JobLocator.class);
            JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
            Job job = jobLocator.getJob(getNome());
            JobParameters params = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();

            jobLauncher.run(job, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
