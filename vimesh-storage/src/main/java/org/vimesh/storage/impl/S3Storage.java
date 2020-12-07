package org.vimesh.storage.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
import org.vimesh.storage.Storage;
import org.vimesh.storage.StorageStat;
import org.vimesh.storage.autoconfigure.StorageProperties.S3Options;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class S3Storage implements Storage {

    private final AmazonS3 client;
    
    public S3Storage(S3Options options) throws Exception {
        AWSCredentials credentials = new BasicAWSCredentials(options.getAccessKey(), options.getSecretKey());
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        if (StringUtils.hasText(options.getEndpoint())) {
            AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                    new AwsClientBuilder.EndpointConfiguration(options.getEndpoint(), options.getRegion());
            this.client = AmazonS3Client.builder()
                    .withEndpointConfiguration(endpointConfiguration)
                    .withCredentials(credentialsProvider)
                    .withPathStyleAccessEnabled(options.isPathStyle())
                    .build();
        } else {
            this.client = AmazonS3Client.builder()
                    .withCredentials(credentialsProvider)
                    .withRegion(options.getRegion())
                    .withPathStyleAccessEnabled(options.isPathStyle())
                    .build();
        }
    }
    
    @Override
    public List<String> listBuckets() throws Exception {
        return client.listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
    }

    @Override
    public boolean hasBucket(String bucket) throws Exception {
        return client.doesBucketExistV2(bucket);
    }

    @Override
    public void createBucket(String bucket) throws Exception {
        client.createBucket(bucket);
    }

    @Override
    public void ensureBucket(String bucket) throws Exception {
        if (!hasBucket(bucket)) {
            createBucket(bucket);
        }
    }

    @Override
    public void deleteBucket(String bucket) throws Exception {
        client.deleteBucket(bucket);
    }

    @Override
    public boolean hasObject(String bucket, String filePath) throws Exception {
        return client.doesObjectExist(bucket, filePath);
    }

    @Override
    public void putObject(String bucket, String filePath, String localFile) throws Exception {
        client.putObject(bucket, filePath, new File(localFile));
    }

    @Override
    public void putObject(String bucket, String filePath, InputStream stream) throws Exception {
        client.putObject(bucket, filePath, stream, null);
    }

    @Override
    public void putObject(String bucket, String filePath, byte[] data) throws Exception {
        try (InputStream stream = new ByteArrayInputStream(data)) {
            putObject(bucket, filePath, stream);
        }
    }

    @Override
    public void getObject(String bucket, String filePath, String localFile) throws Exception {
        client.getObject(new GetObjectRequest(bucket, filePath), new File(localFile));
    }

    @Override
    public InputStream getObject(String bucket, String filePath) throws Exception {
        return client.getObject(bucket, filePath).getObjectContent();
    }

    @Override
    public InputStream getObject(String bucket, String filePath, long offset, long length) throws Exception {
        return client.getObject(new GetObjectRequest(bucket, filePath)
                .withRange(offset, offset + length)).getObjectContent();
    }

    @Override
    public void deleteObject(String bucket, String filePath) throws Exception {
        client.deleteObject(bucket, filePath);
    }

    @Override
    public StorageStat statObject(String bucket, String filePath) throws Exception {
        ObjectMetadata meta = client.getObjectMetadata(bucket, filePath);
        return StorageStat.builder()
                .name(filePath)
                .size(meta.getContentLength())
                .time(meta.getLastModified())
                .build();
    }

    @Override
    public void copyObject(String bucket, String filePath, String srcBucket, String srcFilePath) throws Exception {
        client.copyObject(srcBucket, srcFilePath, bucket, filePath);
    }

    @Override
    public List<StorageStat> listObjects(String bucket, String prefix) throws Exception {
        if (prefix == null) {
            prefix = "";
        }
        ListObjectsV2Result results = client.listObjectsV2(bucket, prefix);
        return results.getObjectSummaries().stream()
                .map(s -> StorageStat.builder()
                        .name(s.getKey())
                        .size(s.getSize())
                        .time(s.getLastModified())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public String getObjectUrl(String bucket, String filePath) throws Exception {
        return client.getUrl(bucket, filePath).toString();
    }

    @Override
    public String getObjectPath(String bucket, String filePath) throws Exception {
        return client.getUrl(bucket, filePath).getPath();
    }
}
