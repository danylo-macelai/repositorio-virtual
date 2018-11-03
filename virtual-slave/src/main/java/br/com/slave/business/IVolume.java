package br.com.slave.business;

import br.com.common.business.IBusiness;
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

}
