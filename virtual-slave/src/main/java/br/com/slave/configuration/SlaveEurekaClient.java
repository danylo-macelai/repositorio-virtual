package br.com.slave.configuration;

import org.springframework.core.env.Environment;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.LeaseInfo;
import com.netflix.appinfo.MyDataCenterInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.config.ConfigurationManager;
import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.CommonConstants;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.Application;

/**
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 14 de dez de 2018
 */
public final class SlaveEurekaClient {

    private static final String EUREKA_CLIENT_HOST = "http.host";
    private static final String EUREKA_CLIENT_PORT = "http.port";
    private static final String EUREKA_REGION      = "eureka.region";
    private static final String EUREKA_SERVER_URL  = "eureka.serviceUrl.default";
    private static final String EUREKA_APP_NAME    = "eureka.app.name";

    private Environment         env;
    private String              serviceIp;
    private String              appName;
    private int                 port;
    private EurekaClient        eurekaClient;
    private String              instanceId;

    public SlaveEurekaClient(Environment env) {
        this.env = env;
        init();
    }

    public EurekaClient getEurekaClient() {
        return eurekaClient;
    }

    public void register(String resourceRootUrl) {

        DataCenterInfo dataCenterInfo = new MyDataCenterInfo(DataCenterInfo.Name.MyOwn);
        EurekaInstanceConfig instanceConfig = new MyDataCenterInstanceConfig(CommonConstants.DEFAULT_CONFIG_NAMESPACE, dataCenterInfo);

        InstanceInfo instanceInfo = info(resourceRootUrl, dataCenterInfo);

        ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);

        EurekaClientConfig config = new DefaultEurekaClientConfig();
        eurekaClient = new DiscoveryClient(applicationInfoManager, config, (AbstractDiscoveryClientOptionalArgs) null);

        eurekaClient.registerHealthCheck(instanceStatus -> {
            return InstanceInfo.InstanceStatus.UP;
        });
    }

    public final String getHomePageUrl() {
        InstanceInfo instance = getApplications().getByInstanceId(instanceId);
        return instance.getHomePageUrl();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public final String getReplicacaoUrl(String dirtyInstanceId) {
        InstanceInfo dirty = null;
        if (dirtyInstanceId != null) {
            dirty = getApplications().getByInstanceId(dirtyInstanceId);
        }
        if (dirty == null) {
            throw new SlaveException("Não existe replicas!!!!!");
        }
        return dirty.getHomePageUrl();
    }

    private void init() {
        serviceIp = env.getProperty(EUREKA_CLIENT_HOST);
        port = Integer.valueOf(env.getProperty(EUREKA_CLIENT_PORT));
        appName = env.getProperty(EUREKA_APP_NAME);
        instanceId = String.format("%s:%s:%s", serviceIp, appName, port);

        ConfigurationManager.getConfigInstance().setProperty(EUREKA_REGION, env.getProperty(EUREKA_REGION));
        ConfigurationManager.getConfigInstance().setProperty(EUREKA_SERVER_URL, env.getProperty(EUREKA_SERVER_URL));
    }

    private Application getApplications() {
        synchronized (SlaveEurekaClient.class) {
            Application registeredApplications = eurekaClient.getApplications().getRegisteredApplications(appName);
            return registeredApplications;
        }
    }

    private InstanceInfo info(String serviceRootUrl, DataCenterInfo dataCenterInfo) {

        InstanceInfo instanceInfo = new InstanceInfo(instanceId,                                /* instanceId */
                appName,                                                                        /* appName */
                "",                                                                             /* appGroupName */
                serviceIp,                                                                      /* ipAddr */
                "na",                                                                           /* sid */
                new InstanceInfo.PortWrapper(true, port),                                       /* port */
                new InstanceInfo.PortWrapper(false, 443),                                       /* securePort */
                String.format("http://%s:%s%s", serviceIp, port, serviceRootUrl),               /* homePageUrl */
                String.format("http://%s:%s%s/status", serviceIp, port, serviceRootUrl),        /* statusPageUrl */
                String.format("http://%s:%s%s/healthCheck", serviceIp, port, serviceRootUrl),   /* healthCheckUrl */
                null,                                                                           /* secureHealthCheckUrl */
                appName,                                                                        /* vipAddress */
                appName,                                                                        /* secureVipAddress */
                1,                                                                              /* countryId */
                dataCenterInfo,                                                                 /* dataCenterInfo */
                serviceIp,                                                                      /* hostName */
                InstanceInfo.InstanceStatus.UP,                                                 /* status */
                InstanceInfo.InstanceStatus.UNKNOWN,                                            /* overriddenStatus */
                null,                                                                           /* overriddenStatusAlt */
                LeaseInfo.Builder.newBuilder().setDurationInSecs(10).build(),                   /* leaseInfo */
                false,                                                                          /* isCoordinatingDiscoveryServer */
                null,                                                                           /* metadata */
                null,                                                                           /* lastUpdatedTimestamp */
                null,                                                                           /* lastDirtyTimestamp */
                InstanceInfo.ActionType.ADDED,                                                  /* actionType */
                ""                                                                              /* asgName */
        );
        return instanceInfo;
    }

}
