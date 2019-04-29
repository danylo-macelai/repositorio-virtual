package br.com.slave.persistence;

import br.com.common.persistence.Persistence;

import br.com.slave.configuration.SlaveException;
import br.com.slave.domain.VolumeTO;

import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 23 de out de 2018
 * @version $
 */
@Repository
public class VolumeDAO extends Persistence<VolumeTO> {

    @Autowired
    public VolumeDAO(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * O método buscar retorna a única instância da classe volume.
     *
     * @return VolumeTO
     */
    public VolumeTO buscar() {
        final StringBuilder hql = new StringBuilder();
        hql.append("SELECT V ");
        hql.append("FROM ").append(VolumeTO.class.getName()).append(" V ");

        final Query query = query(hql);
        return (VolumeTO) query.getSingleResult();
    }

    /**
     * Atualiza algumas informações do volume
     *
     * @param volume
     * @throws SlaveException
     */
    public void update(VolumeTO volume) {
        final StringBuilder hql = new StringBuilder();
        hql.append(" UPDATE ").append(VolumeTO.class.getName()).append(" V ");
        hql.append(" SET V.tamanho = :paramTamanho, ");
        hql.append("     V.contem = :paramContem ");
        hql.append(" WHERE V.id = :paramId ");

        final Query query = query(hql);
        query.setParameter("paramTamanho", volume.getTamanho());
        query.setParameter("paramContem", volume.getContem());
        query.setParameter("paramId", volume.getId());
        query.executeUpdate();
    }

}
