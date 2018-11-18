package br.com.common.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import br.com.common.domain.Domain;
import br.com.common.utils.Utils;

/**
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 22 de out de 2018
 */
public abstract class Persistence<D extends Domain> implements IPersistence<D> {

    private Class<D>                   dominioClass;

    @PersistenceContext
    private EntityManager              em;

    private NamedParameterJdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert           jdbcInsert;

    @Autowired
    public Persistence(DataSource dataSource) {
        this.dominioClass = Utils.actualType(this);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(dominioClass.getAnnotation(Table.class).name())
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * O método {@link Persistence#query(StringBuilder)} cria uma instância de <code> Query </ ​​code> que executa o HQL informado.
     *
     * @param hql
     * @return Query
     */
    protected final Query query(StringBuilder hql) throws DataAccessException {
        return em.createQuery(hql.toString());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void clear() throws DataAccessException {
        em.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void detach(D dominio) throws DataAccessException {
        em.detach(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void flush() throws DataAccessException {
        em.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean contains(D dominio) throws DataAccessException {
        return em.contains(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final List<D> carregarTodos() throws DataAccessException {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT D ");
        hql.append("FROM ").append(dominioClass.getName()).append(" D ");
        return query(hql).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final D ache(long id) throws DataAccessException {
        return em.find(dominioClass, id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final D carregar(long id) throws DataAccessException {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT D ");
        hql.append("FROM ").append(dominioClass.getName()).append(" D ");
        hql.append("WHERE D.id = :paramId ");

        Query query = query(hql);
        query.setParameter("paramId", id);
        return (D) query.getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void incluir(D dominio) throws DataAccessException {
        em.persist(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void alterar(D dominio) throws DataAccessException {
        em.merge(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void excluir(D dominio) throws DataAccessException {
        em.remove(em.contains(dominio) ? dominio : em.merge(dominio));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void excluirTodos() throws DataAccessException {
        em.createQuery("DELETE " + dominioClass.getName()).executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long count() {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT COUNT(D.id) ");
        hql.append("FROM ").append(dominioClass.getName()).append(" D ");

        Query query = query(hql);
        Long count = (Long) query.getSingleResult();
        if (count != null) {
            return count;
        } else {
            return 0L;
        }
    }

}
