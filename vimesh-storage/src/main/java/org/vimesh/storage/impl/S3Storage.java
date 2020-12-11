package org.vimesh.storage.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
import org.vimesh.storage.Storage;
import org.vimesh.storage.StorageStat;
import org.vimesh.storage.autoconfigure.StorageProperties.S3Options;
import org.vimesh.storage.options.S3BucketOptions;
import org.vimesh.storage.options.S3ObjectOptions;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

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
    public void createBucket(String bucket, BucketOptions options) throws Exception {
        if (options != null) {
            Optional<S3BucketOptions> opt = Optional.of(options).map(S3BucketOptions::cast);
            CreateBucketRequest req = new CreateBucketRequest(bucket)
                    .withCannedAcl(opt.map(S3BucketOptions::getCannedAcl).orElse(null));
            client.createBucket(req);
        } else {
            client.createBucket(bucket);
        }
    }

    @Override
    public void ensureBucket(String bucket, BucketOptions options) throws Exception {
        if (!hasBucket(bucket)) {
            createBucket(bucket, options);
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
    public void putObject(String bucket, String filePath, String localFile, ObjectOptions options) throws Exception {
        if (options != null) {
            Optional<S3ObjectOptions> opt = Optional.of(options).map(S3ObjectOptions::cast);
            PutObjectRequest req = new PutObjectRequest(bucket, filePath, new File(localFile))
                    .withMetadata(opt.map(S3ObjectOptions::getMetadata).orElse(null))
                    .withCannedAcl(opt.map(S3ObjectOptions::getCannedAcl).orElse(null))
                    .withStorageClass(opt.map(S3ObjectOptions::getStorageClass).orElse(null));
            client.putObject(req);
        } else {
            client.putObject(bucket, filePath, new File(localFile));
        }
    }

    @Override
    public void putObject(String bucket, String filePath, InputStream stream, ObjectOptions options) throws Exception {
        if (options != null) {
            Optional<S3ObjectOptions> opt = Optional.of(options).map(S3ObjectOptions::cast);
            PutObjectRequest req = new PutObjectRequest(bucket, filePath, stream, 
                    opt.map(S3ObjectOptions::getMetadata).orElse(null))
                    .withCannedAcl(opt.map(S3ObjectOptions::getCannedAcl).orElse(null))
                    .withStorageClass(opt.map(S3ObjectOptions::getStorageClass).orElse(null));
            client.putObject(req);
        } else {
            client.putObject(bucket, filePath, stream, null);
        }
    }

    @Override
    public void putObject(String bucket, String filePath, byte[] data, ObjectOptions options) throws Exception {
        try (InputStream stream = new ByteArrayInputStream(data)) {
            putObject(bucket, filePath, stream, options);
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
        ObjectMetadata metadata = client.getObjectMetadata(bucket, filePath);
        return StorageStat.builder()
                .name(filePath)
                .size(metadata.getContentLength())
                .last(metadata.getLastModified())
                .meta(buildMeta(metadata))
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
                        .last(s.getLastModified())
                        .meta(buildMeta(client.getObjectMetadata(bucket, s.getKey())))
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
    
    private Map<String, String> buildMeta(ObjectMetadata metadata) {
        addUserMetadata(metadata, "cache-control", metadata.getCacheControl());
        addUserMetadata(metadata, "content-disposition", metadata.getContentDisposition());
        addUserMetadata(metadata, "content-encoding", metadata.getContentEncoding());
        addUserMetadata(metadata, "content-language", metadata.getContentLanguage());
        addUserMetadata(metadata, "content-md5", metadata.getContentMD5());
        addUserMetadata(metadata, "content-type", metadata.getContentType());
        return metadata.getUserMetadata();
    }
    
    private void addUserMetadata(ObjectMetadata metadata, String key, String value) {
        if (value != null) {
            metadata.addUserMetadata(key, value);
        }
    }
}
