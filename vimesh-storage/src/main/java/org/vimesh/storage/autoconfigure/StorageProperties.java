package org.vimesh.storage.autoconfigure;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("storage")
@Getter
@Setter
public class StorageProperties {
    
    /**
     * storage enabled
     */
    private boolean enabled;
    /**
     * storage scope list
     */
    private List<StorageOptions> scopes;
    
    @Getter
    @Setter
    public static class StorageOptions {
        
        /**
         * storage name
         */
        private String name;
        /**
         * storage type
         */
        private String type;
        /**
         * storage bucket name
         */
        private String bucket;
        /**
         * storage file path prefix
         */
        private String prefix;
        
        /**
         * if type == "local", need this options
         */
        //private LocalOptions local;
        /**
         * if type == "minio", need this options
         */
        private MinioOptions minio;
    }
    
    /*@Getter
    @Setter
    public static class LocalOptions {
        
    }*/
    
    @Getter
    @Setter
    public static class MinioOptions {
    
        /**
         * Endpoint is an URL, domain name, IPv4 or IPv6 address of S3 service 
         */
        private String endpoint;
        /**
         * TCP/IP port number between 1 and 65535. Unused if endpoint is an URL
         */
        private int port;
        /**
         * Access key (aka user ID) of your account in S3 service
         */
        private String accessKey;
        /**
         * Secret Key (aka password) of your account in S3 service
         */
        private String secretKey;
        /**
         * Region name of buckets in S3 service
         */
        private String region = "";
        /**
         * Flag to indicate to use secure (TLS) connection to S3 service or not
         */
        private boolean secure = false;
    }
}
