package br.com.slave.business.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.common.business.Business;
import br.com.slave.business.ISlaveTask;
import br.com.slave.business.IVolume;
import br.com.slave.configuration.SlaveException;
import br.com.slave.domain.VolumeTO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de abr de 2019
 */
@Service
public class SlaveTaskBusiness extends Business<VolumeTO> implements ISlaveTask {

    @Autowired
    IVolume business;

    /**
     * {@inheritDoc}
     */
    @Override
    public void contagem() throws SlaveException {
        try {
            VolumeTO volume = programmaticTransaction(() -> {
                return business.buscar();
            });
            Path folder = Paths.get(volume.getLocalizacao());
            long size = Files.walk(folder).filter(p -> p.toFile().isFile()).mapToLong(p -> p.toFile().length()).sum();
            long count = Files.walk(folder).filter(p -> p.toFile().isFile()).count();
            volume.reset(count, size);
            programmaticTransaction(() -> {
                business.update(volume);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
