package br.com.master.configuration;

import static br.com.master.configuration.QuartzConfiguration.JOB_GRAVACAO;
import static br.com.master.configuration.QuartzConfiguration.JOB_INSTANCE;
import static br.com.master.configuration.QuartzConfiguration.JOB_REPLICACAO;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.master.business.IMasterTask;
import br.com.master.task.TaskGravacaoSlave;
import br.com.master.task.TaskInstanceSlave;
import br.com.master.task.TaskReplicacaoSlave;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 4 de abr de 2019
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final String STEP_INSTANCE   = "stepInstance";
    private static final String STEP_GRAVACAO   = "stepGravacao";
    private static final String STEP_REPLICACAO = "stepReplicacao";

    @Autowired
    private JobBuilderFactory   jobs;

    @Autowired
    private StepBuilderFactory  steps;

    @Bean
    public Job jobInstanceSlave(IMasterTask masterTaskBusiness) {
        return jobs.get(JOB_INSTANCE). //
                flow(//
                        steps.get(STEP_INSTANCE). //
                                tasklet(new TaskInstanceSlave(masterTaskBusiness)). //
                                build() //

                ). //
                build(). //
                build();
    }

    @Bean
    public Job jobGravacaoSlave(IMasterTask masterTaskBusiness) {
        return jobs.get(JOB_GRAVACAO). //
                flow( //
                        steps.get(STEP_GRAVACAO). //
                                tasklet(new TaskGravacaoSlave(masterTaskBusiness)). //
                                build()

                ). //
                build(). //
                build();
    }

    @Bean
    public Job jobReplicacaoSlave(IMasterTask masterTaskBusiness) {
        return jobs.get(JOB_REPLICACAO). //
                flow( //
                        steps.get(STEP_REPLICACAO). //
                                tasklet(new TaskReplicacaoSlave(masterTaskBusiness)). //
                                build()

                ). //
                build(). //
                build();
    }

}
