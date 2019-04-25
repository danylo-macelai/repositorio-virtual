package br.com.slave.business.impl;

import org.springframework.stereotype.Service;

import br.com.common.business.Business;
import br.com.slave.business.ISlaveTask;
import br.com.slave.configuration.SlaveException;
import br.com.slave.domain.VolumeTO;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de abr de 2019
 */
@Service
public class SlaveTaskBusiness extends Business<VolumeTO> implements ISlaveTask {

    @Override
    public void contagem() throws SlaveException {

        System.out.println("SlaveTaskBusiness.contagem()");

    }

}
