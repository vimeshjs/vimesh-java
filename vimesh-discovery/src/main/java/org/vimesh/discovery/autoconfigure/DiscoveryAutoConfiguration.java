package org.vimesh.discovery.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vimesh.discovery.DiscoveryRunner;
import org.vimesh.discovery.DiscoveryScanner;
import org.vimesh.discovery.DiscoveryService;

@Configuration
@EnableConfigurationProperties(DiscoveryProperties.class)
public class DiscoveryAutoConfiguration {

    @Bean
    @ConditionalOnProperty("discovery.enabled")
    public DiscoveryScanner discoveryScanner() {
        return new DiscoveryScanner();
    }
    
    @Bean
    @ConditionalOnProperty("discovery.enabled")
    public DiscoveryService discoveryService() {
        return new DiscoveryService();
    }
    
    @Bean
    @ConditionalOnProperty("discovery.enabled")
    public DiscoveryRunner discoveryRunner() {
        return new DiscoveryRunner();
    }
}
