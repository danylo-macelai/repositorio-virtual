package br.com.common.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import br.com.common.domain.Domain;
import br.com.common.persistence.IPersistence;

/**
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 22 de out de 2018
 */
public abstract class Business<D extends Domain> implements IBusiness<D> {

    @Autowired
    IPersistence<D> persistence;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<D> carregarTodos() throws DataAccessException {
        return persistence.carregarTodos();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public D ache(long id) throws DataAccessException {
        return persistence.ache(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public D carregar(long id) throws DataAccessException {
        return persistence.carregar(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void incluir(D dominio) throws DataAccessException {
        persistence.incluir(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void alterar(D dominio) throws DataAccessException {
        persistence.alterar(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(D dominio) throws DataAccessException {
        persistence.excluir(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluirTodos() throws DataAccessException {
        persistence.excluirTodos();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long count() throws DataAccessException {
        return persistence.count();
    }

}
