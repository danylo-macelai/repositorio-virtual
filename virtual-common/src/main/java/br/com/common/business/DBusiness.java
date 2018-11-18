package br.com.common.business;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.common.domain.Domain;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-common <br>

 * @author macelai
 * @date: 17 de nov de 2018
 */
public abstract class DBusiness<D extends Domain> implements IBusiness<D> {

    @Autowired
    JpaRepository<D, Long> persistence;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<D> carregarTodos() throws DataAccessException {
        return persistence.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public D ache(long id) throws DataAccessException {
        return persistence.getOne(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public D carregar(long id) throws DataAccessException {
        return persistence.findById(id).orElseThrow(() -> new NoResultException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void incluir(D dominio) throws DataAccessException {
        persistence.save(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void alterar(D dominio) throws DataAccessException {
        persistence.save(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluir(D dominio) throws DataAccessException {
        persistence.delete(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void excluirTodos() throws DataAccessException {
        persistence.deleteAll();
    }

}
