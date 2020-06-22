package com.carota.vimesh.storage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import lombok.Data;

@Data
public class StorageScope {

    private final Storage storage;
    private final String bucket;
    private final String prefix;
    
    public void putObject(String filePath, String localFile) throws Exception {
        storage.putObject(bucket, prefix + filePath, localFile);
    }
    
    public void putObject(String filePath, InputStream stream) throws Exception {
        storage.putObject(bucket, prefix + filePath, stream);
    }
    
    public void putObject(String filePath, byte[] data) throws Exception {
        storage.putObject(bucket, prefix + filePath, data);
    }
    
    public void getObject(String filePath, String localFile) throws Exception {
        storage.getObject(bucket, prefix + filePath, localFile);
    }

    public InputStream getObject(String filePath) throws Exception {
        return storage.getObject(bucket, prefix + filePath);
    }

    public InputStream getObject(String filePath, long offset, long size) throws Exception {
        return storage.getObject(bucket, prefix + filePath, offset, size);
    }

    public void deleteObject(String filePath) throws Exception {
        storage.deleteObject(bucket, prefix + filePath);
    }

    public StorageStat statObject(String filePath) throws Exception {
        return storage.statObject(bucket, prefix + filePath);
    }

    public void copyObject(String filePath, String srcFilePath) throws Exception {
        storage.copyObject(bucket, prefix + filePath, bucket, prefix + srcFilePath);
    }

    public List<StorageStat> listObjects(String otherPrefix) throws Exception {
        if (otherPrefix == null) {
            otherPrefix = "";
        }
        return storage.listObjects(bucket, prefix + otherPrefix);
    }
    
    public byte[] getObjectAsBuffer(String filePath) throws Exception {
        try (InputStream input = getObject(filePath)) {
            return writeToOutput(input).toByteArray();
        }
    }
    
    public byte[] getObjectAsBuffer(String filePath, long offset, long size) throws Exception {
        try (InputStream input = getObject(filePath, offset, size)) {
            return writeToOutput(input).toByteArray();
        }
    }
    
    public String getObjectAsString(String filePath) throws Exception {
        try (InputStream input = getObject(filePath)) {
            return writeToOutput(input).toString();
        }
    }
    
    public String getObjectAsString(String filePath, long offset, long size) throws Exception {
        try (InputStream input = getObject(filePath, offset, size)) {
            return writeToOutput(input).toString();
        }
    }
    
    public void getBucketObject(String bucket, String filePath, String localFile) throws Exception {
        storage.getObject(bucket, prefix + filePath, localFile);
    }

    public InputStream getBucketObject(String bucket, String filePath) throws Exception {
        return storage.getObject(bucket, prefix + filePath);
    }

    public InputStream getBucketObject(String bucket, String filePath, long offset, long size) throws Exception {
        return storage.getObject(bucket, prefix + filePath, offset, size);
    }
    
    public byte[] getBucketObjectAsBuffer(String bucket, String filePath) throws Exception {
        try (InputStream input = getBucketObject(bucket, filePath)) {
            return writeToOutput(input).toByteArray();
        }
    }
    
    public byte[] getBucketObjectAsBuffer(String bucket, String filePath, long offset, long size) throws Exception {
        try (InputStream input = getBucketObject(bucket, filePath, offset, size)) {
            return writeToOutput(input).toByteArray();
        }
    }
    
    public String getBucketObjectAsString(String bucket, String filePath) throws Exception {
        try (InputStream input = getBucketObject(bucket, filePath)) {
            return writeToOutput(input).toString();
        }
    }
    
    public String getBucketObjectAsString(String bucket, String filePath, long offset, long size) throws Exception {
        try (InputStream input = getBucketObject(bucket, filePath, offset, size)) {
            return writeToOutput(input).toString();
        }
    }
    
    private ByteArrayOutputStream writeToOutput(InputStream input) throws Exception {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            return output;
        }
    }
}
