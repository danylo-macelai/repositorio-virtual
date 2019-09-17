/*
 * Copyright (C) 2019
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.proxy.configuration;

import java.util.List;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-proxy <br>
 *
 * @author macelai
 * @date: 19 de set de 2019
 * @version $
 */
public class LoadBalancerRuleSlave extends AbstractLoadBalancerRule {

    
    
    int count        = 0;
    int currentIndex = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public Server choose(Object key) {
        return this.choose(this.getLoadBalancer(), key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initWithNiwsConfig(IClientConfig arg0) {

    }

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) { 
            return null;
        } else {
            Server server = null;

            while (server == null) {
                if (Thread.interrupted()) {
                    return null;
                }

                List<Server> upList = lb.getReachableServers();
                List<Server> allList = lb.getAllServers();
                int serverCount = allList.size();
                if (serverCount == 0) {
                    return null;
                }

                // int index = this.rand.nextInt(serverCount);
                // server = (Server)upList.get(index);

                if (count < 5) {
                    count++;
                    server = upList.get(currentIndex);
                } else {
                    count = 0;
                    currentIndex++;
                    if (currentIndex >= serverCount) {
                        currentIndex = 0;
                    }
                }

                if (server == null) {
                    Thread.yield();
                } else {
                    if (server.isAlive()) {
                        return server;
                    }

                    server = null;
                    Thread.yield();
                }
            }

            return server;
        }
    }

}