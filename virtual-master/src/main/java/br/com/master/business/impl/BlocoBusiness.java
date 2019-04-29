package br.com.master.business.impl;

import br.com.common.business.DBusiness;

import br.com.master.business.IBloco;
import br.com.master.configuration.MasterBalance;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;
import br.com.master.domain.BlocoTO;
import br.com.master.persistence.BlocoDAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 16 de nov de 2018
 * @version $
 */
@Service
public class BlocoBusiness extends DBusiness<BlocoTO> implements IBloco {

    @Autowired
    BlocoDAO persistence;

    @Autowired
    MasterBalance balance;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BlocoTO> carregarTodosPor(ArquivoTO arquivo) throws MasterException {
        return persistence.findByArquivoOrderByNumeroAsc(arquivo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BlocoTO> carregarParaGravacao() throws MasterException {
        final List<BlocoTO> itens = new ArrayList<>();
        for (final Object[] values : persistence.carregarParaGracacao()) {
            final BlocoTO bloco = new BlocoTO();
            bloco.setId(Long.valueOf(values[0].toString()));
            bloco.setUuid(values[1].toString());
            bloco.setDiretorioOffLine(values[2].toString());
            itens.add(bloco);
        }
        return itens;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BlocoTO> carregarParaReplicacao() throws MasterException {
        final List<BlocoTO> itens = new ArrayList<>();
        for (final Object[] values : persistence.carregarParaReplicacao()) {
            final BlocoTO bloco = new BlocoTO();
            bloco.setNumero(Integer.valueOf(values[1].toString()));
            bloco.setUuid(values[2].toString());
            bloco.setTamanho(Integer.valueOf(values[3].toString()));
            bloco.setDiretorioOffLine(values[4].toString());
            bloco.setInstanceId(values[5].toString());
            bloco.setReplica(true);
            final ArquivoTO arquivo = new ArquivoTO();
            arquivo.setId(Long.valueOf(values[6].toString()));
            bloco.setArquivo(arquivo);
            itens.add(bloco);
        }
        return itens;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BlocoTO> carregarParaExclusao() throws MasterException {
        final List<BlocoTO> itens = new ArrayList<>();
        for (final Object[] values : persistence.carregarParaExclusao()) {
            final BlocoTO bloco = new BlocoTO();
            bloco.setUuid(values[0].toString());
            bloco.setInstanceId(values[1].toString());
            itens.add(bloco);
        }
        return itens;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(String uuid, String instanceId) throws MasterException {
        return persistence.existsByUuidAndInstanceId(uuid, instanceId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void update(BlocoTO bloco) throws MasterException {
        persistence.update(bloco.getInstanceId(), bloco.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public void excluiBloco(BlocoTO bloco) throws MasterException {
        persistence.deleteByUuidAndInstanceId(bloco.getUuid(), bloco.getInstanceId());
    }

}
