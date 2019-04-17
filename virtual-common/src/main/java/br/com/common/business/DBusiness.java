package br.com.common.business;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.com.common.domain.Domain;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-common <br>

 * @author macelai
 * @date: 17 de nov de 2018
 */
public abstract class DBusiness<D extends Domain> implements IBusiness<D> {

    @Autowired
    JpaRepository<D, Long>          persistence;

    @Autowired
    protected JpaTransactionManager txManager;

    /**
     * Crie uma nova transaction com o dado comportamento de propagação, suspendendo a transação atual, se houver.
     *
     * @return TransactionDefinition
     * @throws DataAccessException
     */
    protected final TransactionDefinition getTransactionDefinition() throws DataAccessException {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName("Controle manual de Transaction");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return definition;
    }

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

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() throws DataAccessException {
        return persistence.count();
    }

}
