package br.com.master.wrappers;

import static br.com.common.configuration.CommonQuartzConfiguration.APPLICATION_CONTEXT;

import br.com.common.wrappers.CommonJob;

import java.io.Serializable;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 4 de abr de 2019
 * @version $
 */
@SuppressWarnings("serial")
public class MasterJob extends CommonJob implements Serializable {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            final ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext()
                    .get(APPLICATION_CONTEXT);
            final JobLocator jobLocator = applicationContext.getBean(JobLocator.class);
            final JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
            final Job job = jobLocator.getJob(getNome());
            final JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();

            jobLauncher.run(job, params);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
