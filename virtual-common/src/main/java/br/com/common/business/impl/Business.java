package br.com.common.business.impl;

import java.util.List;

import br.com.common.business.IBusiness;
import br.com.common.domain.Domain;
import br.com.common.persistence.IPersistence;

/**
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 22 de out de 2018
 */
public abstract class Business<D extends Domain> implements IBusiness<D> {

    IPersistence<D> persistence;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<D> carregarTodos() {
        return persistence.carregarTodos();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public D ache(long id) {
        return persistence.ache(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public D carregar(long id) {
        return persistence.carregar(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void incluir(D dominio) {
        persistence.incluir(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alterar(D dominio) {
        persistence.alterar(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void excluir(D dominio) {
        persistence.excluir(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void excluirTodos() {
        persistence.excluirTodos();
    }

}
