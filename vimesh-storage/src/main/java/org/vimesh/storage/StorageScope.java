package org.vimesh.storage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import lombok.Data;

@Data
public class StorageScope {

    private final Storage storage;
    private final String bucket;
    private final String prefix;
    
    public boolean hasObject(String filePath) throws Exception {
        return storage.hasObject(bucket, prefix + filePath);
    }
    
    public void putObject(String filePath, String localFile) throws Exception {
        storage.putObject(bucket, prefix + filePath, localFile, null);
    }
    
    public void putObject(String filePath, String localFile, Storage.ObjectOptions options) throws Exception {
        storage.putObject(bucket, prefix + filePath, localFile, options);
    }
    
    public void putObject(String filePath, InputStream stream) throws Exception {
        storage.putObject(bucket, prefix + filePath, stream, null);
    }
    
    public void putObject(String filePath, InputStream stream, Storage.ObjectOptions options) throws Exception {
        storage.putObject(bucket, prefix + filePath, stream, options);
    }
    
    public void putObject(String filePath, byte[] data) throws Exception {
        storage.putObject(bucket, prefix + filePath, data, null);
    }
    
    public void putObject(String filePath, byte[] data, Storage.ObjectOptions options) throws Exception {
        storage.putObject(bucket, prefix + filePath, data, options);
    }
    
    public void getObject(String filePath, String localFile) throws Exception {
        storage.getObject(bucket, prefix + filePath, localFile);
    }

    public InputStream getObject(String filePath) throws Exception {
        return storage.getObject(bucket, prefix + filePath);
    }
    
    public InputStream getObject(String filePath, long offset) throws Exception {
        return storage.getObject(bucket, prefix + filePath, offset, null);
    }

    public InputStream getObject(String filePath, long offset, long length) throws Exception {
        return storage.getObject(bucket, prefix + filePath, offset, length);
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
    
    public String getObjectUrl(String filePath) throws Exception {
        return storage.getObjectUrl(bucket, prefix + filePath);
    }
    
    public String getObjectPath(String filePath) throws Exception {
        return storage.getObjectPath(bucket, prefix + filePath);
    }
    
    public byte[] getObjectAsBuffer(String filePath) throws Exception {
        try (InputStream input = getObject(filePath)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toByteArray();
            }
        }
    }
    
    public byte[] getObjectAsBuffer(String filePath, long offset) throws Exception {
        try (InputStream input = getObject(filePath, offset)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toByteArray();
            }
        }
    }
    
    public byte[] getObjectAsBuffer(String filePath, long offset, long length) throws Exception {
        try (InputStream input = getObject(filePath, offset, length)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toByteArray();
            }
        }
    }
    
    public String getObjectAsString(String filePath) throws Exception {
        try (InputStream input = getObject(filePath)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toString();
            }
        }
    }
    
    public String getObjectAsString(String filePath, long offset) throws Exception {
        try (InputStream input = getObject(filePath, offset)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toString();
            }
        }
    }
    
    public String getObjectAsString(String filePath, long offset, long length) throws Exception {
        try (InputStream input = getObject(filePath, offset, length)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toString();
            }
        }
    }
    
    public boolean hasBucketObject(String bucket, String filePath) throws Exception {
        return storage.hasObject(bucket, prefix + filePath);
    }
    
    public void getBucketObject(String bucket, String filePath, String localFile) throws Exception {
        storage.getObject(bucket, prefix + filePath, localFile);
    }

    public InputStream getBucketObject(String bucket, String filePath) throws Exception {
        return storage.getObject(bucket, prefix + filePath);
    }
    
    public InputStream getBucketObject(String bucket, String filePath, long offset) throws Exception {
        return storage.getObject(bucket, prefix + filePath, offset, null);
    }

    public InputStream getBucketObject(String bucket, String filePath, long offset, long length) throws Exception {
        return storage.getObject(bucket, prefix + filePath, offset, length);
    }
    
    public byte[] getBucketObjectAsBuffer(String bucket, String filePath) throws Exception {
        try (InputStream input = getBucketObject(bucket, filePath)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toByteArray();
            }
        }
    }
    
    public byte[] getBucketObjectAsBuffer(String bucket, String filePath, long offset) throws Exception {
        try (InputStream input = getBucketObject(bucket, filePath, offset)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toByteArray();
            }
        }
    }
    
    public byte[] getBucketObjectAsBuffer(String bucket, String filePath, long offset, long length) throws Exception {
        try (InputStream input = getBucketObject(bucket, filePath, offset, length)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toByteArray();
            }
        }
    }
    
    public String getBucketObjectAsString(String bucket, String filePath) throws Exception {
        try (InputStream input = getBucketObject(bucket, filePath)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toString();
            }
        }
    }
    
    public String getBucketObjectAsString(String bucket, String filePath, long offset) throws Exception {
        try (InputStream input = getBucketObject(bucket, filePath, offset)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toString();
            }
        }
    }
    
    public String getBucketObjectAsString(String bucket, String filePath, long offset, long length) throws Exception {
        try (InputStream input = getBucketObject(bucket, filePath, offset, length)) {
            try (ByteArrayOutputStream output = writeToOutput(input)) {
                return output.toString();
            }
        }
    }
    
    private ByteArrayOutputStream writeToOutput(InputStream input) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
        return output;
    }
}
