package com.carota.vimesh.portlet;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.carota.vimesh.portlet.client.KeyValueClient;
import com.carota.vimesh.grpc.autoconfigure.GRpcServerProperties;
import com.carota.vimesh.portlet.autoconfigure.PortletProperties;
import com.carota.vimesh.portlet.utils.InetUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PortletService {

    private static final String NAME_PREFIX = "portlets/@";
    
    @Autowired
    private PortletProperties portletProperties;
    @Autowired
    private GRpcServerProperties gRpcServerProperties;
    @Autowired
    private PortletScanner portletScanner;
    
    private String[] serviceNames;
    private String selfUrl;
    private KeyValueClient kvClient;
    private Map<Class<?>, GRpcClient<?>> serviceClients;
    
    protected void startup() {
        long now = System.currentTimeMillis();
        serviceNames = portletScanner.getServices().stream()
                .map(name -> NAME_PREFIX + name + "/" + now).toArray(String[]::new);
        selfUrl = findSelfUrl().toString();
        
        PortletProperties.Url discoveryUrl = portletProperties.getDiscovery().getUrl();
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
                long grace = portletProperties.getDiscovery().getShutdownGrace().getSeconds();
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
        // write self address into discovery
        Duration expiration = portletProperties.getDiscovery().getExpiration();
        for (String name : serviceNames) {
            kvClient.set(name, selfUrl, expiration.getSeconds() + "s");
        }
        
        // fetch the newest service map
        if (portletScanner.getClients().isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        portletScanner.getClients().forEach((cls, builder) -> {
            Map<String, String> serviceMap = kvClient.get(NAME_PREFIX + builder.getName() + "/*");
            GRpcClient<?> client = serviceClients.get(cls);
            if (serviceMap.isEmpty()) {
                // no service available
                serviceClients.remove(cls);
                shutdownClient(client);
                return;
            }
            if (client == null 
                    || !serviceMap.containsKey(client.getServiceName())
                    || client.getExpireTime() < now) {
                // service no longer available or service is expired
                switchClient(cls, client, serviceMap, now + expiration.toMillis());
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    public <T extends GRpcClient<?>> T getClient(Class<T> cls) {
        GRpcClient<?> client = serviceClients.get(cls);
        if (client == null) {
            log.warn("No service found for gRPC client {}", cls.getName());
            return null;
        }
        return (T)client;
    }
    
    private GRpcClient<?> switchClient(
            Class<?> cls, GRpcClient<?> client, Map<String, String> serviceMap, long expireTime) {
        String serviceName = selectServiceName(serviceMap);
        
        // still selected the previous client
        if (client != null && client.getServiceName().equals(serviceName)) {
            client.setExpireTime(expireTime);
            return client;
        }
        
        // create a new client and shutdown the previous client
        try {
            String serviceAddr = serviceMap.get(serviceName);
            PortletProperties.Url serviceUrl = toUrl(serviceAddr);
            GRpcClientBuilder builder = portletScanner.getClients().get(cls);
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
            long grace = portletProperties.getDiscovery().getShutdownGrace().getSeconds();
            client.shutdown(grace);
        } catch (InterruptedException e) {
            log.error(String.format("gRPC client for %s interrupted during destroy.", client.getServiceName()), e);
        }
        log.info("gRPC client for {} destroyed.", client.getServiceName());
    }
    
    private PortletProperties.Url findSelfUrl() {
        try {
            // prefer configure address if giving
            if (portletProperties.getSelfUrl() != null) {
                return portletProperties.getSelfUrl();
            }
            // find local address
            PortletProperties.Url url = new PortletProperties.Url();
            url.setHost(InetUtils.findLocalAddress().getHostAddress());
            url.setPort(gRpcServerProperties.getRunningPort());
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
    
    private PortletProperties.Url toUrl(String address) {
        try {
            String[] parts = address.split(":");
            PortletProperties.Url url = new PortletProperties.Url();
            url.setHost(parts[0]);
            url.setPort(Integer.valueOf(parts[1]));
            return url;
        } catch (NumberFormatException e) {
            log.error("string to url interrupted.", e);
            return null;
        }
    }
}