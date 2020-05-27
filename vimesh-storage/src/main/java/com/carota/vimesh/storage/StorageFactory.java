package com.carota.vimesh.storage;

import com.carota.vimesh.storage.autoconfigure.StorageProperties;
import com.carota.vimesh.storage.impl.LocalStorage;
import com.carota.vimesh.storage.impl.MinioStorage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StorageFactory {
    
    protected static Storage createStorage(StorageProperties.StorageOptions options) {
        switch (options.getType().toLowerCase()) {
        case "local":
            try {
                return new LocalStorage();
            } catch (Exception e) {
                log.error("Cannot create storage of type \"local\"", e);
                return null;
            }
        case "minio":
            try {
                return new MinioStorage(options.getMinio());
            } catch (Exception e) {
                log.error("Cannot create storage of type \"minio\"", e);
                return null;
            }
        default:
            log.error("Storage type \"{}\" is not supported", options.getType());
            return null;
        }
    }
    
    protected static StorageScope createStorageScope(Storage storage, String bucket, String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        return new StorageScope(storage, bucket, prefix);
    }
}
