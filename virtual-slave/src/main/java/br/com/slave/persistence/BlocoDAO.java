package br.com.slave.persistence;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.common.persistence.Persistence;
import br.com.slave.domain.BlocoTO;

/**
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 23 de out de 2018
 */
@Repository
public class BlocoDAO extends Persistence<BlocoTO> {

    @Autowired
    public BlocoDAO(DataSource dataSource) {
        super(dataSource);
    }

}
