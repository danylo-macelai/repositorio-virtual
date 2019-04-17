package br.com.master.business;

import java.util.List;

import br.com.common.business.IBusiness;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;
import br.com.master.domain.BlocoTO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 16 de nov de 2018
 */
public interface IBloco extends IBusiness<BlocoTO> {

    /**
     * Carrega todos os blocos que pertence ao arquivo
     *
     * @param arquivo
     * @return List<BlocoTO>
     * @throws MasterException
     */
    List<BlocoTO> carregarTodosPor(ArquivoTO arquivo) throws MasterException;

    /**
     * Carrega todos os blocos que estão apenas no diretório local {@link BlocoTO#getDiretorioOffLine()} e não possuem {@link BlocoTO#getInstanceId()}
     *
     * @return List<BlocoTO>
     * @throws MasterException List<BlocoTO>
     */
    List<BlocoTO> carregarParaGravacao() throws MasterException;

    /**
     * Carrega os blocos que deverão ser replicados em outras instâncias slaves
     *
     * @return List<BlocoTO>
     * @throws MasterException
     */
    List<BlocoTO> carregarParaReplicacao() throws MasterException;

    /**
     * Verifica se já existe um bloco na instância
     *
     * @param uuid
     * @param instanceId
     * @return boolean
     */
    boolean exists(String uuid, String instanceId);

    /**
     * Atualiza algumas informações do bloco
     *
     * @param bloco
     */
    void updateBloco(BlocoTO bloco);

}
