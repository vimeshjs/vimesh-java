package org.vimesh.discovery;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.vimesh.discovery.autoconfigure.DiscoveryProperties;

public class DiscoveryRunner implements CommandLineRunner, DisposableBean {

    @Autowired
    private DiscoveryProperties discoveryProperties;
    @Autowired
    private DiscoveryService discoveryService;
    
    private ScheduledExecutorService refreshExecutor;
    
    public DiscoveryRunner() {
        this.refreshExecutor = Executors.newSingleThreadScheduledExecutor();
    }
    
    @Override
    public void run(String... args) throws Exception {
        discoveryService.startup();
        
        long interval = discoveryProperties.getRefreshInterval().toMillis();
        refreshExecutor.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                discoveryService.refresh();
            }
            
        }, 0L, interval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void destroy() throws Exception {
        refreshExecutor.shutdown();
        
        discoveryService.shutdown();
    }
}
