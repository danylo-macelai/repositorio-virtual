package br.com.master.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

import br.com.master.wrappers.Slave;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 27 de mar de 2019
 */
@Component
public class MasterBalance {

    private static final String EUREKA_APP_CLIENT_NAME = "eureka.app.client.name";

    private Set<Slave>          slaves;
    private Environment         env;
    private String              appName;

    @Autowired
    PeerAwareInstanceRegistry   registry;

    public MasterBalance(Environment env) {
        this.env = env;
        init();
    }

    /**
     * Obtém uma instanceId.
     * <p>
     * Observe que o tráfego será direcionado para a instância que possui maior capacidade de armazenamento. </ p>
     *
     * @return String página inicial
     */
    public final String nextVolumeInstanceIdSlave() {
        return slaves.stream().sorted((x, y) -> {
            return Integer.compare(x.getContem(), y.getContem());
        }).findFirst().orElse(new Slave()).usage().getInstanceId();

    }

    /**
     * Obtém a home page definida para esta instância.
     *
     * @param instanceId
     * @return String página inicial
     */
    public final String volumeUrlSlave(String instanceId) {
        InstanceInfo instance = registry.getInstanceByAppAndId(appName, instanceId);
        if (instance == null) {
            throw new MasterException("instance.id.e.invalido");
        }
        return instance.getHomePageUrl();
    }

    /**
     * Preenche a lista de instâncias registadas para Virtual-Slave
     * <p>
     * Observe que se a instância já existir sua quantidade/capacidade será atualizada.
     * </p>
     *
     * @param function - função responsável por atualizar as instâncias
     */
    public final void atualizarInstanceId(Function<InstanceInfo, Slave> function) {
        Application registeredApplications = registry.getApplications().getRegisteredApplications(appName);
        if (registeredApplications != null) {
            for (InstanceInfo info : registeredApplications.getInstances()) {
                try {
                    Slave slave = function.apply(info);
                    if (!slaves.add(slave)) {
                        slaves.stream().filter(v -> v.getInstanceId().equals(slave.getInstanceId())).forEach(s -> {
                            s.setContem(slave.getContem());
                        });
                    }
                } catch (Exception e) {
                    slaves.removeIf(v -> v.getInstanceId().equals(info.getInstanceId()));
                }
            }
        }
    }

    /**
     * Percorre todas as instâncias que estão associadas ao Virtual-Slave.
     * <p>
     * Observe que as instâncias serão ordenadas por quantidade/capacidade de arquivos para direcionar o tráfego para a instância que possui maior
     * capacidade de armazenamento. </ p>
     *
     * @param function - função responsável por consumir as informações
     */
    public final void percorrerInstanceId(Function<String, Boolean> function) {
        List<Slave> instances = slaves.stream().sorted((s1, s2) -> Integer.compare(s1.getContem(), s2.getContem())).collect(Collectors.toList());
        for (Slave slave : instances) {
            if (function.apply(slave.getInstanceId())) {
                break;
            }
        }
    }

    private void init() {

        slaves = new HashSet<>();

        appName = env.getProperty(EUREKA_APP_CLIENT_NAME);
    }

}
