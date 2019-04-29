package br.com.master.task;

import br.com.master.business.IMasterTask;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 5 de abr de 2019
 * @version $
 */
public class TaskInstanceSlave implements Tasklet {

    static AtomicBoolean running = new AtomicBoolean(false);
    IMasterTask          business;

    public TaskInstanceSlave(IMasterTask masterTaskBusiness) {
        business = masterTaskBusiness;
    }

    @Override
    public RepeatStatus execute(StepContribution step, ChunkContext chunk) throws Exception {
        if (!running.getAndSet(true)) {
            try {
                business.instance();
            } catch (final Exception e) {
                e.printStackTrace();
            } finally {
                running.set(false);
            }
        }
        return RepeatStatus.FINISHED;
    }

}
