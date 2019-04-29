package br.com.slave.business;

import br.com.slave.configuration.SlaveException;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de abr de 2019
 * @version $
 */
public interface ISlaveTask {

    /**
     * Executa a tarefa que realiza a contagem dos blocos e calcula o tamanho do volume
     *
     * @throws MasterException
     */
    void contagem() throws SlaveException;

}
