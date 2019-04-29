package br.com.slave.business.impl;

import br.com.common.business.Business;

import br.com.slave.business.ISlaveTask;
import br.com.slave.business.IVolume;
import br.com.slave.configuration.SlaveException;
import br.com.slave.domain.VolumeTO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de abr de 2019
 * @version $
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
            final VolumeTO volume = programmaticTransaction(() -> {
                return business.buscar();
            });
            final Path folder = Paths.get(volume.getLocalizacao());
            final long size = Files.walk(folder).filter(p -> p.toFile().isFile()).mapToLong(p -> p.toFile().length())
                    .sum();
            final long count = Files.walk(folder).filter(p -> p.toFile().isFile()).count();
            volume.reset(count, size);
            programmaticTransaction(() -> {
                business.update(volume);
            });
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
