package br.com.master.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.common.business.DBusiness;
import br.com.master.business.IBloco;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;
import br.com.master.domain.BlocoTO;
import br.com.master.persistence.BlocoDAO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 16 de nov de 2018
 */
@Service
public class BlocoBusiness extends DBusiness<BlocoTO> implements IBloco {

    @Autowired
    BlocoDAO persistence;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BlocoTO> carregarTodosPor(ArquivoTO arquivo) throws MasterException {
        return persistence.findByArquivoOrderByNumeroAsc(arquivo);
    }

}