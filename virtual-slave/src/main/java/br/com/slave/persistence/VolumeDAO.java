package br.com.slave.persistence;

import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.common.persistence.Persistence;
import br.com.slave.domain.VolumeTO;

/**
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 23 de out de 2018
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
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT V ");
        hql.append("FROM ").append(VolumeTO.class.getName()).append(" V ");

        Query query = query(hql);
        return (VolumeTO) query.getSingleResult();
    }

}
