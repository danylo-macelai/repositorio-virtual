package br.com.slave.business;

import javax.ws.rs.core.StreamingOutput;

import org.wso2.msf4j.Request;

import br.com.common.business.IBusiness;
import br.com.common.wrappers.File;
import br.com.slave.configuration.SlaveException;
import br.com.slave.domain.VolumeTO;

/**
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 23 de out de 2018
 */
public interface IVolume extends IBusiness<VolumeTO> {

    /**
     * O método buscar é um Singleton que retorna a única instância da classe volume.
     *
     * @return VolumeTO
     * @throws SlaveException
     */
    VolumeTO buscar() throws SlaveException;

    /**
     * Gera um identificador único para o bloco que será salvo no volume
     *
     * @param request
     * @return File
     * @throws SlaveException
     */
    File upload(Request request) throws SlaveException;

    /**
     * Realiza o download do bloco com o uuid informado
     *
     * @param uuid
     * @param stream
     * @throws SlaveException
     */
    StreamingOutput download(String uuid) throws SlaveException;

    /**
     * Cria uma cópia do bloco
     *
     * @param uuid
     * @param instanceId
     * @return FileTO
     * @throws SlaveException
     */
    File replicacao(String uuid, String instanceId) throws SlaveException;

    /**
     * Exclui apenas o bloco com uuid informado.
     *
     * @param uuid
     */
    void excluir(String uuid) throws SlaveException;

}
