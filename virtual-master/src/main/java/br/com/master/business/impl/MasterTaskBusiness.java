package br.com.master.business.impl;

import static br.com.common.utils.Utils.VIRTUAL_EXTENSION;
import static br.com.common.utils.Utils.fileRemover;
import static br.com.common.utils.Utils.httpDelete;
import static br.com.common.utils.Utils.httpGet;
import static br.com.common.utils.Utils.httpPost;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.common.business.DBusiness;
import br.com.master.business.IBloco;
import br.com.master.business.IMasterTask;
import br.com.master.configuration.MasterBalance;
import br.com.master.configuration.MasterException;
import br.com.master.domain.BlocoTO;
import br.com.master.wrappers.Slave;
import okhttp3.Response;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 15 de abr de 2019
 */
@Service
public class MasterTaskBusiness extends DBusiness<BlocoTO> implements IMasterTask {

    static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Autowired
    IBloco                    business;

    @Autowired
    MasterBalance             balance;

    /**
     * {@inheritDoc}
     */
    @Override
    public void instance() throws MasterException {
        balance.atualizarInstanceId(info -> {
            try (Response response = httpGet(info.getHomePageUrl())) {
                Slave slave = mapper.readValue(response.body().string(), Slave.class);
                slave.setInstanceId(info.getInstanceId());
                return slave;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gravacao() throws MasterException {
        Iterator<BlocoTO> blocos = programmaticTransaction(() -> {
            return business.carregarParaGravacao().iterator();
        });

        while (blocos.hasNext()) {
            BlocoTO bloco = blocos.next();
            bloco.setInstanceId(balance.nextVolumeInstanceIdSlave());
            if (bloco.getInstanceId() == null) {
                break;
            }

            programmaticTransaction(() -> {
                try (Response response = httpPost(new FileInputStream(bloco.getDiretorioOffLine()), balance.volumeUrlSlave(bloco.getInstanceId()),
                        "gravacao", bloco.getUuid())) {
                    if (Status.NO_CONTENT.getStatusCode() != response.code()) {
                        throw new MasterException(response.body().string()).status(Status.BAD_REQUEST);
                    }
                } catch (Exception e) {
                    throw new MasterException(e.getMessage());
                }
                business.updateBloco(bloco);
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replicacao() throws MasterException {
        Iterator<BlocoTO> blocos = programmaticTransaction(() -> {
            return business.carregarParaReplicacao().iterator();
        });

        Map<String, String> params = new HashMap<>();
        while (blocos.hasNext()) {
            BlocoTO bloco = blocos.next();
            String instanceIdOrigem = bloco.getInstanceId();
            balance.percorrerInstanceId(instanceId -> {
                if (!instanceIdOrigem.equals(instanceId)) {
                    boolean exists = programmaticTransaction(() -> {
                        return business.exists(bloco.getUuid(), instanceId);
                    });
                    if (!exists) {
                        params.clear();
                        params.put("instance_id", bloco.getInstanceId());
                        return programmaticTransaction(() -> {
                            try (Response response = httpPost(balance.volumeUrlSlave(instanceIdOrigem), "replicacao", bloco.getUuid(), params)) {
                                if (Status.NO_CONTENT.getStatusCode() != response.code()) {
                                    throw new MasterException(response.body().string()).status(Status.BAD_REQUEST);
                                }
                            } catch (Exception e) {
                                throw new MasterException(e.getMessage());
                            }
                            bloco.setInstanceId(instanceId);
                            business.incluir(bloco);
                            return true;
                        });
                    }
                }
                return false;
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void limpar() throws MasterException {
        File directory = new File(balance.getPathTmpDirectory());
        File[] files = directory.listFiles(file -> !file.getName().toLowerCase().endsWith(VIRTUAL_EXTENSION));
        for (File file : files) {
            fileRemover(file);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exclusao() throws MasterException {
        Iterator<BlocoTO> blocos = programmaticTransaction(() -> {
            return business.carregarParaExclusao().iterator();
        });

        while (blocos.hasNext()) {
            BlocoTO bloco = blocos.next();
            programmaticTransaction(() -> {
                try (Response response = httpDelete(balance.volumeUrlSlave(bloco.getInstanceId()), "exclusao", bloco.getUuid())) {
                    if (Status.NO_CONTENT.getStatusCode() != response.code()) {
                        throw new MasterException(response.body().string()).status(Status.BAD_REQUEST);
                    }
                } catch (Exception e) {
                    throw new MasterException(e.getMessage());
                }
                business.excluiBloco(bloco);
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void migracao() throws MasterException {

        System.out.println("\n\n\n NÃO IMPLEMENTADO[MasterTaskBusiness.migracao()]! \n\n\n");

    }

}
