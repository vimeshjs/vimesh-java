package org.vimesh.storage;

import java.io.InputStream;
import java.util.List;

public interface Storage {

    List<String> listBuckets() throws Exception;
    boolean hasBucket(String bucket) throws Exception;
    void createBucket(String bucket) throws Exception;
    void ensureBucket(String bucket) throws Exception;
    void deleteBucket(String bucket) throws Exception;
    
    void putObject(String bucket, String filePath, String localFile) throws Exception;
    void putObject(String bucket, String filePath, InputStream stream) throws Exception;
    void putObject(String bucket, String filePath, byte[] data) throws Exception;
    void getObject(String bucket, String filePath, String localFile) throws Exception;
    InputStream getObject(String bucket, String filePath) throws Exception;
    InputStream getObject(String bucket, String filePath, long offset, long length) throws Exception;
    void deleteObject(String bucket, String filePath) throws Exception;
    StorageStat statObject(String bucket, String filePath) throws Exception;
    void copyObject(String bucket, String filePath, String srcBucket, String srcFilePath) throws Exception;
    List<StorageStat> listObjects(String bucket, String prefix) throws Exception;
}
