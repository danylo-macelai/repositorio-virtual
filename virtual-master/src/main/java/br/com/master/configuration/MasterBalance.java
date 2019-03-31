package br.com.master.configuration;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

import br.com.common.utils.Utils;
import okhttp3.Response;

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

    private Timer               usageChecker;
    private ObjectMapper        mapper;
    private Set<Instance>       slaves;
    private Environment         env;
    private String              appName;

    @Autowired
    PeerAwareInstanceRegistry   registry;

    public MasterBalance(Environment env) {
        this.env = env;
        init();
    }

    public String nextVolumeUrlSlave() {
        return volumeUrlSlave(nextVolumeInstanceIdSlave());
    }

    public String nextVolumeInstanceIdSlave() {
        return next().getInstanceId();
    }

    public String volumeUrlSlave(String instanceId) {
        InstanceInfo instance = registry.getInstanceByAppAndId(appName, instanceId);
        if (instance == null) {
            throw new MasterException("instance.id.e.invalido");
        }
        return instance.getHomePageUrl();
    }

    /**
     * @return Volume
     */
    private Instance next() {
        Instance volume = slaves.stream().sorted((x, y) -> {
            System.out.println(x.getInstanceId() + " : " + x.getContem() + " - " + y.getInstanceId() + " : " + y.getContem());
            return Integer.compare(x.getContem(), y.getContem());
        }).findFirst().orElse(new Instance()).usage();
        return volume;
    }

    private void init() {
        usageChecker = new Timer();
        usageChecker.schedule(new UsageChecker(), 60 * 1000, 10 * 1000);

        slaves = new HashSet<>();

        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        appName = env.getProperty(EUREKA_APP_CLIENT_NAME);
    }

    /**
     * <b>Description:</b> <br>
     * <b>Project:</b> virtual-master <br>
     *
     * @author macelai
     * @date: 27 de mar de 2019
     */
    private class UsageChecker extends TimerTask {
        @Override
        public void run() {
            Application registeredApplications = registry.getApplications().getRegisteredApplications(appName);
            for (InstanceInfo applicationsInstance : registeredApplications.getInstances()) {
                Predicate<Instance> isQualified = v -> v.getInstanceId().equals(applicationsInstance.getInstanceId());
                try {
                    Response response = Utils.httpGet(applicationsInstance.getHomePageUrl());
                    Instance volume = mapper.readValue(response.body().string(), Instance.class);
                    volume.setInstanceId(applicationsInstance.getInstanceId());
                    if (!slaves.add(volume)) {
                        slaves.stream().filter(isQualified).forEach(v -> {
                            v.setContem(volume.getContem());
                        });
                    }
                } catch (Exception e) {
                    slaves.removeIf(isQualified);
                }
            }
        }
    }

    /**
     * <b>Description:</b> <br>
     * <b>Project:</b> virtual-master <br>
     *
     * @author macelai
     * @date: 29 de mar de 2019
     */
    @SuppressWarnings("serial")
    private static class Instance implements Serializable {

        private int    contem;
        private String instanceId;

        public Instance usage() {
            contem++;
            return this;
        }

        public int getContem() {
            return contem;
        }

        public void setContem(int contem) {
            this.contem = contem;
        }

        public String getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }

        @Override
        public final boolean equals(Object other) {
            if ((other == null) || !this.getClass().equals(other.getClass())) {
                return false;
            }
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(instanceId, ((Instance) other).getInstanceId());
            return builder.isEquals();
        }

        /**
         * @return
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
            return result;
        }
    }

}
