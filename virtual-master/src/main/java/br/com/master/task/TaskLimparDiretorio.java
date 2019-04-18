package br.com.master.task;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import br.com.master.business.IMasterTask;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de abr de 2019
 */
public class TaskLimparDiretorio implements Tasklet {

    static AtomicBoolean running = new AtomicBoolean(false);
    IMasterTask           business;

    public TaskLimparDiretorio(IMasterTask masterTaskBusiness) {
        business = masterTaskBusiness;
    }

    @Override
    public RepeatStatus execute(StepContribution step, ChunkContext chunk) throws Exception {
        if (!running.getAndSet(true)) {
            try {
                business.limpar();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                running.set(false);
            }
        }
        return RepeatStatus.FINISHED;
    }

}
