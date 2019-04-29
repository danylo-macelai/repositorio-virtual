package br.com.common.business;

import br.com.common.domain.Domain;
import br.com.common.persistence.IPersistence;
import br.com.common.wrappers.WithResult;
import br.com.common.wrappers.WithoutResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 22 de out de 2018
 * @version $
 */
public abstract class Business<D extends Domain> implements IBusiness<D> {

    @Autowired
    IPersistence<D> persistence;

    @Autowired
    TransactionTemplate transactionTemplate;

    protected <T> T programmaticTransaction(WithResult<T> with) {
        return transactionTemplate.execute(new TransactionCallback<T>() {
            @Override
            public T doInTransaction(TransactionStatus status) {
                return with.get();
            }
        });
    }

    protected void programmaticTransaction(WithoutResult without) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                without.get();
            }
        });
    }

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
