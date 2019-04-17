package br.com.master.business;

import br.com.master.configuration.MasterException;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 15 de abr de 2019
 */
public interface IMasterTask {
    
    /**
     * Executa a tarefa que obtêm as instâncias do virtual slave
     *
     * @throws MasterException
     */
    void instance() throws MasterException;
    
    /**
     * Envia os blocos que estão no diretório para uma instância de slave
     *
     * @throws MasterException
     */
    void gravacao() throws MasterException;
    
    /**
     * Replica os blocos que estão armazenados
     *
     * @throws MasterException
     */
    void replicacao() throws MasterException;
    
}
