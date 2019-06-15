package br.com.master.task;

import static br.com.common.configuration.CommonQuartzConfiguration.APPLICATION_CONTEXT;

import br.com.master.business.IMasterTask;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 5 de abr de 2019
 * @version $
 */
@SuppressWarnings("serial")
public class TaskInstanceSlave extends QuartzJobBean implements Serializable {

    static AtomicBoolean running = new AtomicBoolean(false);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (!running.getAndSet(true)) {
            try {
                final ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext()
                        .get(APPLICATION_CONTEXT);
                final IMasterTask business = applicationContext.getBean(IMasterTask.class);
                business.instance();
            } catch (final Exception e) {
                e.printStackTrace();
            } finally {
                running.set(false);
            }
        }
    }

}
