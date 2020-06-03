package com.carota.vimesh.portlet.autoconfigure;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("portlet")
@Getter
@Setter
public class PortletProperties {

    /**
     * portlet enabled
     */
    private boolean enabled;
    /**
     * current service address, will auto detect it if not giving
     */
    private Url selfUrl = null;
    /**
     * discovery configuration
     */
    private Discovery discovery;
    
    @Getter
    @Setter
    public static class Discovery {
        
        /**
         * discovery address
         */
        private Url url;
        /**
         * address expiration time
         */
        private Duration expiration = Duration.ofMinutes(1);
        /**
         * time interval to send address to discovery
         */
        private Duration refreshInterval = Duration.ofSeconds(3);
        /**
         * shutdown timeout of GRPC client
         */
        private Duration shutdownGrace = Duration.ofSeconds(5);
    }
    
    @Getter
    @Setter
    public static class Url {

        private String host;
        private int port;
        
        @Override
        public String toString() {
            return host + ":" + port;
        }
    }
}
