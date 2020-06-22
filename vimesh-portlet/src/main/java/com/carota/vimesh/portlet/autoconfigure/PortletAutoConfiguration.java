package com.carota.vimesh.portlet.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.carota.vimesh.portlet.PortletService;
import com.carota.vimesh.portlet.PortletRunner;
import com.carota.vimesh.portlet.PortletScanner;

@Configuration
@EnableConfigurationProperties(PortletProperties.class)
public class PortletAutoConfiguration {

    @Bean
    @ConditionalOnProperty("discovery.enabled")
    public PortletScanner portletScanner() {
        return new PortletScanner();
    }
    
    @Bean
    @ConditionalOnProperty("discovery.enabled")
    public PortletService portletService() {
        return new PortletService();
    }
    
    @Bean
    @ConditionalOnProperty("discovery.enabled")
    public PortletRunner portletRunner() {
        return new PortletRunner();
    }
}
