package br.com.master.business;

import br.com.common.business.IBusiness;

import br.com.master.configuration.MasterException;
import br.com.master.domain.ConfiguracaoTO;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 * @version $
 */
public interface IConfiguracao extends IBusiness<ConfiguracaoTO> {

    /**
     * O método buscar é um singleton que retorna a única instância da classe configuracao.
     *
     * @return ConfiguracaoTO
     * @throws MasterException
     */
    ConfiguracaoTO buscar() throws MasterException;

}
