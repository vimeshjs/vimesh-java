package org.vimesh.storage.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vimesh.storage.Storage;
import org.vimesh.storage.StorageStat;
import org.vimesh.storage.autoconfigure.StorageProperties.MinioOptions;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

public class MinioStorage implements Storage {

    private final MinioClient client;
    private final MinioOptions options;
    
    public MinioStorage(MinioOptions options) throws Exception {
        this.client = new MinioClient(
                options.getEndpoint(),
                options.getPort(),
                options.getAccessKey(),
                options.getSecretKey(),
                options.getRegion(),
                options.isSecure());
        this.options = options;
    }
    
    @Override
    public List<String> listBuckets() throws Exception {
        return Arrays.asList(client.listBuckets().stream().map(Bucket::name).toArray(String[]::new));
    }
    
    @Override
    public boolean hasBucket(String bucket) throws Exception {
        return client.bucketExists(bucket);
    }
    
    @Override
    public void createBucket(String bucket) throws Exception {
        client.makeBucket(bucket, options.getRegion());
    }
    
    @Override
    public void ensureBucket(String bucket) throws Exception {
        if (!hasBucket(bucket)) {
            createBucket(bucket);
        }
    }
    
    @Override
    public void deleteBucket(String bucket) throws Exception {
        client.removeBucket(bucket);
    }
    
    @Override
    public void putObject(String bucket, String filePath, String localFile) throws Exception {
        client.putObject(bucket, filePath, localFile, null);
    }
    
    @Override
    public void putObject(String bucket, String filePath, InputStream stream) throws Exception {
        client.putObject(bucket, filePath, stream, new PutObjectOptions(stream.available(), -1));
    }
    
    @Override
    public void putObject(String bucket, String filePath, byte[] data) throws Exception {
        InputStream stream = new ByteArrayInputStream(data);
        putObject(bucket, filePath, stream);
    }
    
    @Override
    public void getObject(String bucket, String filePath, String localFile) throws Exception {
        client.getObject(bucket, filePath, localFile);
    }
    
    @Override
    public InputStream getObject(String bucket, String filePath) throws Exception {
        return client.getObject(bucket, filePath);
    }
    
    @Override
    public InputStream getObject(String bucket, String filePath, long offset, long size) throws Exception {
        return client.getObject(bucket, filePath, offset, size);
    }
    
    @Override
    public void deleteObject(String bucket, String filePath) throws Exception {
        client.removeObject(bucket, filePath);
    }
    
    @Override
    public StorageStat statObject(String bucket, String filePath) throws Exception {
        ObjectStat stat = client.statObject(bucket, filePath);
        return new StorageStat(stat.name(), stat.length(), stat.createdTime());
    }
    
    @Override
    public void copyObject(String bucket, String filePath, String srcBucket, String srcFilePath) throws Exception {
        client.copyObject(bucket, filePath, null, null, srcBucket, srcFilePath, null, null);
    }
    
    @Override
    public List<StorageStat> listObjects(String bucket, String prefix) throws Exception {
        if (prefix == null) {
            prefix = "";
        }
        Iterable<Result<Item>> results = client.listObjects(bucket, prefix);
        List<StorageStat> list = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            if (item.isDir()) {
                continue;
            }
            list.add(new StorageStat(item.objectName(), item.size(), item.lastModified()));
        }
        return list;
    }
}
