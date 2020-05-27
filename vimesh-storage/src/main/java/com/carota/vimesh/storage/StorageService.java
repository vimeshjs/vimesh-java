package com.carota.vimesh.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.carota.vimesh.storage.autoconfigure.StorageProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageService {

    private static final String DEFAULT_BUCKET = "default";
    
    @Autowired
    private StorageProperties storageProperties;
    
    private Map<String, StorageScope> scopes;
    private StorageScope defaultScope;
    
    @PostConstruct
    private void init() {
        scopes = new ConcurrentHashMap<>();
        
        storageProperties.getScopes().forEach(options -> {
            try {
                Storage storage = StorageFactory.createStorage(options);
                if (storage == null) {
                    return;
                }
                
                String bucket = options.getBucket();
                if (bucket == null || bucket.isEmpty()) {
                    bucket = DEFAULT_BUCKET;
                }
                storage.ensureBucket(bucket);
                
                StorageScope scope = StorageFactory.createStorageScope(storage, bucket, options.getPrefix());
                scopes.put(options.getName(), scope);
            } catch (Exception e) {
                log.error(String.format("Initialize storage \"%s\" interrupted", options.getName()), e);
                return;
            }
        });
        
        if (!storageProperties.getScopes().isEmpty()) {
            String defaultName = storageProperties.getScopes().get(0).getName();
            defaultScope = scopes.get(defaultName);
        }
        
        log.info("Storage service initialized");
    }
    
    public StorageScope getScope(String name) {
        return scopes.get(name);
    }
    
    public StorageScope getScope() {
        return defaultScope;
    }
}
