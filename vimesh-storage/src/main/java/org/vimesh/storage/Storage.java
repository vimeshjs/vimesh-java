package org.vimesh.storage;

import java.io.InputStream;
import java.util.List;

public interface Storage {

    interface BucketOptions {}
    interface ObjectOptions {}
    
    List<String> listBuckets() throws Exception;
    boolean hasBucket(String bucket) throws Exception;
    void createBucket(String bucket, BucketOptions options) throws Exception;
    void ensureBucket(String bucket, BucketOptions options) throws Exception;
    void deleteBucket(String bucket) throws Exception;
    boolean hasObject(String bucket, String filePath) throws Exception;
    void putObject(String bucket, String filePath, String localFile, ObjectOptions options) throws Exception;
    void putObject(String bucket, String filePath, InputStream stream, ObjectOptions options) throws Exception;
    void putObject(String bucket, String filePath, byte[] data, ObjectOptions options) throws Exception;
    void getObject(String bucket, String filePath, String localFile) throws Exception;
    InputStream getObject(String bucket, String filePath) throws Exception;
    InputStream getObject(String bucket, String filePath, Long offset, Long length) throws Exception;
    void deleteObject(String bucket, String filePath) throws Exception;
    StorageStat statObject(String bucket, String filePath) throws Exception;
    void copyObject(String bucket, String filePath, String srcBucket, String srcFilePath) throws Exception;
    List<StorageStat> listObjects(String bucket, String prefix) throws Exception;
    String getObjectHeader(String bucket, String filePath, String key) throws Exception;
    String getObjectHeader(String bucket, String filePath, String key, Long offset, Long length) throws Exception;
    String getObjectUrl(String bucket, String filePath) throws Exception;
    String getObjectPath(String bucket, String filePath) throws Exception;
}
