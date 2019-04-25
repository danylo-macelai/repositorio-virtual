package br.com.slave.task;

import static br.com.common.configuration.CommonQuartzConfiguration.APPLICATION_CONTEXT;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import br.com.common.wrappers.CommonJob;
import br.com.slave.business.ISlaveTask;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de abr de 2019
 */
@SuppressWarnings("serial")
public class TaskContagem extends CommonJob implements Serializable {
    
    static AtomicBoolean running = new AtomicBoolean(false);
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (!running.getAndSet(true)) {
            try {
                ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext().get(APPLICATION_CONTEXT);
                ISlaveTask business = applicationContext.getBean(ISlaveTask.class);
                business.contagem();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                running.set(false);
            }
        }
    }
    
}
