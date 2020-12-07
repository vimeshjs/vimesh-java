package org.vimesh.discovery;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.vimesh.discovery.autoconfigure.DiscoveryProperties;
import org.vimesh.discovery.client.KeyValueClient;
import org.vimesh.discovery.utils.InetUtils;
import org.vimesh.grpc.autoconfigure.GRpcServerProperties;
import org.vimesh.grpc.client.GRpcClient;
import org.vimesh.grpc.client.GRpcClientBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiscoveryService {

    private static final String SERVICE_PREFIX = "services/@";
    private static final String PORTLET_PREFIX = "portlets/@";
    
    @Autowired(required=false)
    private GRpcServerProperties gRpcServerProperties;
    @Autowired
    private DiscoveryProperties discoveryProperties;
    @Autowired
    private DiscoveryScanner discoveryScanner;
    
    private String[] serviceNames;
    private String selfUrl;
    private KeyValueClient kvClient;
    private Map<Class<?>, GRpcClient<?>> serviceClients;
    
    protected void startup() {
        long now = System.currentTimeMillis();
        serviceNames = discoveryScanner.getServices().stream()
                .map(name -> SERVICE_PREFIX + name + "/" + now).toArray(String[]::new);
        selfUrl = findSelfUrl().toString();
        
        DiscoveryProperties.Url discoveryUrl = discoveryProperties.getUrl();
        kvClient = new KeyValueClient(discoveryUrl.getHost(), discoveryUrl.getPort());
        serviceClients = new ConcurrentHashMap<>();
        
        log.info("Create gRPC client connecting KeyValueService at {}", discoveryUrl.toString());
    }
    
    protected void shutdown() {
        // shutdown key value client and remove self from discovery
        Optional.ofNullable(kvClient).ifPresent(client -> {
            log.info("Shutting down gRPC client for KeyValueService ...");
            try {
                for (String name : serviceNames) {
                    client.del(name);
                }
                long grace = discoveryProperties.getShutdownGrace().getSeconds();
                client.shutdown(grace);
            } catch (InterruptedException e) {
                log.error("gRPC client for KeyValueService interrupted during destroy.", e);
            }
            log.info("gRPC client for KeyValueService destroyed.");
        });
        
        // shutdown all service clients
        Optional.ofNullable(serviceClients).ifPresent(clients -> {
            clients.values().forEach(client -> {
                shutdownClient(client);
            });
        });
    }
    
    protected void refresh() {
        try {
            // write self address into discovery
            String selfDuration = getSelfDuration();
            for (String name : serviceNames) {
                kvClient.set(name, selfUrl, selfDuration);
            }
            
            // fetch the newest service map
            if (discoveryScanner.getClients().isEmpty()) {
                return;
            }
            Instant now = Instant.now();
            discoveryScanner.getClients().forEach((cls, builder) -> {
                Map<String, String> serviceMap = kvClient.get(SERVICE_PREFIX + builder.getName() + "/*");
                GRpcClient<?> client = serviceClients.get(cls);
                if (CollectionUtils.isEmpty(serviceMap)) {
                    // no service available
                    serviceClients.remove(cls);
                    shutdownClient(client);
                    return;
                }
                if (client == null || !serviceMap.containsKey(client.getServiceName()) || client.isExpired(now)) {
                    // service no longer available or service is expired
                    switchClient(cls, client, serviceMap, getNextExpireTime(now));
                }
            });
        } catch (Exception e) {
            log.error("Exception in discovery refresh", e);
        }
    }
    
    public <T extends GRpcClient<?>> T getClient(Class<T> cls) {
        if (serviceClients == null || cls == null) {
            return null;
        }
        GRpcClient<?> client = serviceClients.get(cls);
        if (client == null) {
            log.warn("No service found for gRPC client {}", cls.getName());
            return null;
        }
        return cls.cast(client);
    }
    
    public String getPortletUrl(String key) {
        if (kvClient == null || key == null) {
            return null;
        }
        Map<String, String> serviceMap = kvClient.get(PORTLET_PREFIX + key);
        if (CollectionUtils.isEmpty(serviceMap)) {
            return null;
        }
        String serviceName = selectServiceName(serviceMap);
        return serviceMap.get(serviceName);
    }
    
    private GRpcClient<?> switchClient(
            Class<?> cls, GRpcClient<?> client, Map<String, String> serviceMap, Instant expireTime) {
        String serviceName = selectServiceName(serviceMap);
        
        // still selected the previous client
        if (client != null && client.getServiceName().equals(serviceName)) {
            client.setExpireTime(expireTime);
            return client;
        }
        
        // create a new client and shutdown the previous client
        try {
            String serviceAddr = serviceMap.get(serviceName);
            DiscoveryProperties.Url serviceUrl = toUrl(serviceAddr);
            GRpcClientBuilder builder = discoveryScanner.getClients().get(cls);
            GRpcClient<?> newClient = builder.newClient();
            newClient.init(serviceName, serviceUrl.getHost(), serviceUrl.getPort(), builder);
            newClient.setExpireTime(expireTime);
            
            serviceClients.put(cls, newClient);
            shutdownClient(client);
            
            log.info("Create gRPC client connecting {} at {}", serviceName, serviceAddr);
            return newClient;
        } catch (Exception e) {
            log.error("create new grpc client interrupted.", e);
            return null;
        }
    }
    
    private void shutdownClient(GRpcClient<?> client) {
        if (client == null) {
            return;
        }
        
        log.info("Shutting down gRPC client for {} ...", client.getServiceName());
        try {
            long grace = discoveryProperties.getShutdownGrace().getSeconds();
            client.shutdown(grace);
        } catch (InterruptedException e) {
            log.error(String.format("gRPC client for %s interrupted during destroy.", client.getServiceName()), e);
        }
        log.info("gRPC client for {} destroyed.", client.getServiceName());
    }
    
    private DiscoveryProperties.Url findSelfUrl() {
        try {
        	DiscoveryProperties.Url url = new DiscoveryProperties.Url();
            // prefer configure address if giving
            if (discoveryProperties.getSelfUrl() != null) {
                url.setHost(discoveryProperties.getSelfUrl().getHost());
                url.setPort(discoveryProperties.getSelfUrl().getPort());
            } else {
                // find local address
                url.setHost(InetUtils.findLocalAddress().getHostAddress());
            }
            if (url.getPort() == 0 && gRpcServerProperties != null) {
                url.setPort(gRpcServerProperties.getRunningPort());
            }
            return url;
        } catch (Exception e) {
            log.error("find self url interrupted.", e);
            return null;
        }
    }
    
    private String selectServiceName(Map<String, String> serviceMap) {
        String[] keys = serviceMap.keySet().toArray(new String[0]);
        if (keys.length == 1) {
            return keys[0];
        }
        int index = new Random().nextInt(keys.length);
        return keys[index];
    }
    
    private DiscoveryProperties.Url toUrl(String address) {
        try {
            String[] parts = address.split(":");
            DiscoveryProperties.Url url = new DiscoveryProperties.Url();
            url.setHost(parts[0]);
            url.setPort(Integer.valueOf(parts[1]));
            return url;
        } catch (NumberFormatException e) {
            log.error("string to url interrupted.", e);
            return null;
        }
    }
    
    private String getSelfDuration() {
        Duration duration = discoveryProperties.getSelfDuration();
        if (duration.isZero() || duration.isNegative()) {
            return "";
        }
        return duration.getSeconds() + "s";
    }
    
    private Instant getNextExpireTime(Instant now) {
        Duration expiration = discoveryProperties.getExpiration();
        if (expiration.isZero() || expiration.isNegative()) {
            return null;
        }
        return now.plusSeconds(expiration.getSeconds());
    }
}
