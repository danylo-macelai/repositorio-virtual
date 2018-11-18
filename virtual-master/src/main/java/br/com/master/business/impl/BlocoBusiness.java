package br.com.master.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.common.business.DBusiness;
import br.com.master.business.IBloco;
import br.com.master.domain.BlocoTO;
import br.com.master.persistence.BlocoDAO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 16 de nov de 2018
 */
@Service
public class BlocoBusiness extends DBusiness<BlocoTO> implements IBloco {

    @Autowired
    BlocoDAO persistence;

}