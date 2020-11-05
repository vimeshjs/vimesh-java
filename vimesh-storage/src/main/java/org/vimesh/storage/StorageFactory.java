package org.vimesh.storage;

import org.vimesh.storage.autoconfigure.StorageProperties;
import org.vimesh.storage.impl.LocalStorage;
import org.vimesh.storage.impl.MinioStorage;
import org.vimesh.storage.impl.S3Storage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StorageFactory {
    
    protected static Storage createStorage(StorageProperties.StorageOptions options) {
        switch (options.getType().toLowerCase()) {
        case "local":
            try {
                return new LocalStorage(options.getLocal());
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
        case "s3":
            try {
                return new S3Storage(options.getS3());
            } catch (Exception e) {
                log.error("Cannot create storage of type \"s3\"", e);
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
