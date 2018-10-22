package br.com.common.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.common.domain.Domain;
import br.com.common.utils.Utils;

/**
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 22 de out de 2018
 */
public abstract class Persistence<D extends Domain> implements IPersistence<D> {

    private Class<D>      dominioClass;

    @PersistenceContext
    private EntityManager em;

    public Persistence() {
        this.dominioClass = Utils.actualType(this);
    }

    /**
     * O método {@link Persistence#query(StringBuilder)} cria uma instância de <code> Query </ ​​code> que executa o HQL informado.
     *
     * @param hql
     * @return Query
     */
    protected final Query query(StringBuilder hql) {
        return em.createQuery(hql.toString());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final List<D> carregarTodos() {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT D ");
        hql.append("FROM ").append(dominioClass.getName()).append(" D ");
        return query(hql).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final D ache(long id) {
        return em.find(dominioClass, id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final D carregar(long id) {
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
    public final void incluir(D dominio) {
        em.persist(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void alterar(D dominio) {
        em.merge(dominio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void excluir(D dominio) {
        em.remove(em.contains(dominio) ? dominio : em.merge(dominio));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void excluirTodos() {
        em.createQuery("DELETE " + dominioClass.getName()).executeUpdate();
    }

}
