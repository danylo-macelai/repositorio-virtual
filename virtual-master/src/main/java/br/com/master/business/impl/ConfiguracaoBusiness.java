package br.com.master.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.common.business.DBusiness;
import br.com.master.business.IConfiguracao;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ConfiguracaoTO;
import br.com.master.persistence.ConfiguracaoDAO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 */
@Service
public class ConfiguracaoBusiness extends DBusiness<ConfiguracaoTO> implements IConfiguracao {

    ConfiguracaoTO  INSTANCE;

    @Autowired
    ConfiguracaoDAO persistence;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ConfiguracaoTO buscar() throws MasterException {
        if (INSTANCE == null) {
            INSTANCE = persistence.buscar();
        }
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void alterar(ConfiguracaoTO configuracao) throws MasterException {
        super.alterar(configuracao);
        INSTANCE = null;
    }

}
