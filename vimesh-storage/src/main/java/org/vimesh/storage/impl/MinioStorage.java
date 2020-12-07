package org.vimesh.storage.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.vimesh.storage.Storage;
import org.vimesh.storage.StorageStat;
import org.vimesh.storage.autoconfigure.StorageProperties.MinioOptions;

import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.DownloadObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import okhttp3.HttpUrl;

public class MinioStorage implements Storage {

    private final MinioClient client;
    
    public MinioStorage(MinioOptions options) throws Exception {
        HttpUrl url = HttpUrl.parse(options.getEndpoint());
        if (options.getPort() > 0) {
            url = url.newBuilder().port(options.getPort()).build();
        }
        this.client = MinioClient.builder()
                .endpoint(url)
                .credentials(options.getAccessKey(), options.getSecretKey())
                .build();
    }
    
    @Override
    public List<String> listBuckets() throws Exception {
        return client.listBuckets().stream().map(Bucket::name).collect(Collectors.toList());
    }
    
    @Override
    public boolean hasBucket(String bucket) throws Exception {
        return client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    }
    
    @Override
    public void createBucket(String bucket) throws Exception {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
    }
    
    @Override
    public void ensureBucket(String bucket) throws Exception {
        if (!hasBucket(bucket)) {
            createBucket(bucket);
        }
    }
    
    @Override
    public void deleteBucket(String bucket) throws Exception {
        client.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
    }
    
    @Override
    public boolean hasObject(String bucket, String filePath) throws Exception {
        try {
            client.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(filePath)
                    .build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        }
    }

    @Override
    public void putObject(String bucket, String filePath, String localFile) throws Exception {
        client.uploadObject(UploadObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .filename(localFile)
                .build());
    }
    
    @Override
    public void putObject(String bucket, String filePath, InputStream stream) throws Exception {
        client.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .stream(stream, stream.available(), -1)
                .build());
    }
    
    @Override
    public void putObject(String bucket, String filePath, byte[] data) throws Exception {
        try (InputStream stream = new ByteArrayInputStream(data)) {
            putObject(bucket, filePath, stream);
        }
    }
    
    @Override
    public void getObject(String bucket, String filePath, String localFile) throws Exception {
        client.downloadObject(DownloadObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .filename(localFile)
                .build());
    }
    
    @Override
    public InputStream getObject(String bucket, String filePath) throws Exception {
        return client.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .build());
    }
    
    @Override
    public InputStream getObject(String bucket, String filePath, long offset, long size) throws Exception {
        return client.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .offset(offset)
                .length(size)
                .build());
    }
    
    @Override
    public void deleteObject(String bucket, String filePath) throws Exception {
        client.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .build());
    }
    
    @Override
    public StorageStat statObject(String bucket, String filePath) throws Exception {
        ObjectStat stat = client.statObject(StatObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .build());
        return StorageStat.builder()
                .name(stat.name())
                .size(stat.length())
                .time(Date.from(stat.createdTime().toInstant()))
                .build();
    }
    
    @Override
    public void copyObject(String bucket, String filePath, String srcBucket, String srcFilePath) throws Exception {
        client.copyObject(CopyObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .source(CopySource.builder()
                        .bucket(srcBucket)
                        .object(srcFilePath)
                        .build())
                .build());
    }
    
    @Override
    public List<StorageStat> listObjects(String bucket, String prefix) throws Exception {
        if (prefix == null) {
            prefix = "";
        }
        Iterable<Result<Item>> results = client.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(prefix)
                .build());
        List<StorageStat> list = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            if (item.isDir()) {
                continue;
            }
            list.add(StorageStat.builder()
                    .name(item.objectName())
                    .size(item.size())
                    .time(Date.from(item.lastModified().toInstant()))
                    .build());
        }
        return list;
    }

    @Override
    public String getObjectUrl(String bucket, String filePath) throws Exception {
        return client.getObjectUrl(bucket, filePath);
    }

    @Override
    public String getObjectPath(String bucket, String filePath) throws Exception {
        return new URL(client.getObjectUrl(bucket, filePath)).getPath();
    }
}
