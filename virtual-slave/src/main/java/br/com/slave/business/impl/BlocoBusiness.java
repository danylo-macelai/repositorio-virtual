package br.com.slave.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.common.business.impl.Business;
import br.com.slave.business.IBloco;
import br.com.slave.domain.BlocoTO;
import br.com.slave.persistence.BlocoDAO;

/**
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 23 de out de 2018
 */
@Service
public class BlocoBusiness extends Business<BlocoTO> implements IBloco {

    @Autowired
    BlocoDAO persistence;

}
