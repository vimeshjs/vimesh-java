package org.vimesh.discovery.autoconfigure;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("discovery")
@Getter
@Setter
public class DiscoveryProperties {

    /**
     * discovery client enabled
     */
    private boolean enabled;
    /**
     * package for scanning GRPC
     */
    private String scan;
    /**
     * discovery service address
     */
    private Url url;
    /**
     * current service address, will auto detect it if not giving
     */
    private Url selfUrl = null;
    /**
     * address expiration time
     */
    private Duration expiration = Duration.ofMinutes(1);
    /**
     * time interval to sync address to discovery
     */
    private Duration refreshInterval = Duration.ofSeconds(3);
    /**
     * shutdown timeout of GRPC client
     */
    private Duration shutdownGrace = Duration.ofSeconds(5);
    
    @Getter
    @Setter
    public static class Url {

        private String host;
        private int port;
        
        @Override
        public String toString() {
            return port != 0 ? host + ":" + port : host;
        }
    }
}
