package com.carota.vimesh.portlet;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.carota.vimesh.portlet.autoconfigure.PortletProperties;

public class PortletRunner implements CommandLineRunner, DisposableBean {

    @Autowired
    private PortletProperties portletProperties;
    @Autowired
    private PortletService portlet;
    
    private ScheduledExecutorService refreshExecutor;
    
    public PortletRunner() {
        this.refreshExecutor = Executors.newSingleThreadScheduledExecutor();
    }
    
    @Override
    public void run(String... args) throws Exception {
        portlet.startup();
        
        long interval = portletProperties.getRefreshInterval().toMillis();
        refreshExecutor.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                portlet.refresh();
            }
            
        }, 0L, interval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void destroy() throws Exception {
        refreshExecutor.shutdown();
        
        portlet.shutdown();
    }
}
