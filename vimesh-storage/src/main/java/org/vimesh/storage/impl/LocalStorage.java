package org.vimesh.storage.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.vimesh.storage.Storage;
import org.vimesh.storage.StorageStat;
import org.vimesh.storage.autoconfigure.StorageProperties.LocalOptions;

public class LocalStorage implements Storage {

    private final File root;
    
    public LocalStorage(LocalOptions options) throws Exception {
        this.root = Paths.get(options.getRoot()).toFile();
        if (this.root.exists()) {
            Assert.state(this.root.isDirectory(), "Root exists but not a directory");
        } else {
            Assert.state(this.root.mkdirs(), "Root directory cannot be created");
        }
    }
    
    @Override
    public List<String> listBuckets() throws Exception {
        return Arrays.asList(root.listFiles(File::isDirectory)).stream()
                .map(File::getName).collect(Collectors.toList());
    }

    @Override
    public boolean hasBucket(String bucket) throws Exception {
        File f = Paths.get(root.getPath(), bucket).toFile();
        return f.exists() && f.isDirectory();
    }

    @Override
    public void createBucket(String bucket) throws Exception {
        File f = Paths.get(root.getPath(), bucket).toFile();
        Assert.state(f.mkdir(), "Bucket directory cannot be created");
    }

    @Override
    public void ensureBucket(String bucket) throws Exception {
        File f = Paths.get(root.getPath(), bucket).toFile();
        if (f.exists()) {
            Assert.state(f.isDirectory(), "Bucket exists but not a directory");
        } else {
            Assert.state(f.mkdir(), "Bucket directory cannot be created");
        }
    }

    @Override
    public void deleteBucket(String bucket) throws Exception {
        File f = Paths.get(root.getPath(), bucket).toFile();
        if (f.exists()) {
            Assert.state(f.delete(), "Bucket directory cannot be deleted");
        }
    }

    @Override
    public void putObject(String bucket, String filePath, String localFile) throws Exception {
        Path src = Paths.get(localFile);
        Path dst = Paths.get(root.getPath(), bucket, filePath);
        dst.getParent().toFile().mkdirs();
        Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void putObject(String bucket, String filePath, InputStream stream) throws Exception {
        Path dst = Paths.get(root.getPath(), bucket, filePath);
        dst.getParent().toFile().mkdirs();
        Files.copy(stream, dst, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void putObject(String bucket, String filePath, byte[] data) throws Exception {
        try (InputStream stream = new ByteArrayInputStream(data)) {
            putObject(bucket, filePath, stream);
        }
    }

    @Override
    public void getObject(String bucket, String filePath, String localFile) throws Exception {
        Path src = Paths.get(root.getPath(), bucket, filePath);
        Path dst = Paths.get(localFile);
        dst.getParent().toFile().mkdirs();
        Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public InputStream getObject(String bucket, String filePath) throws Exception {
        File file = Paths.get(root.getPath(), bucket, filePath).toFile();
        return new FileInputStream(file);
    }

    @Override
    public InputStream getObject(String bucket, String filePath, long offset, long length) throws Exception {
        try (InputStream stream = getObject(bucket, filePath)) {
            int len = Long.valueOf(length).intValue();
            byte[] data = new byte[len];
            stream.skip(offset);
            stream.read(data, 0, len);
            return new ByteArrayInputStream(data);
        }
    }

    @Override
    public void deleteObject(String bucket, String filePath) throws Exception {
        File file = Paths.get(root.getPath(), bucket, filePath).toFile();
        if (file.exists()) {
            Assert.state(file.delete(), "Object file cannot be deleted");
        }
    }

    @Override
    public StorageStat statObject(String bucket, String filePath) throws Exception {
        File file = Paths.get(root.getPath(), bucket, filePath).toFile();
        return new StorageStat(file.getName(), file.length(), 
                Date.from(Instant.ofEpochMilli(file.lastModified())));
    }

    @Override
    public void copyObject(String bucket, String filePath, String srcBucket, String srcFilePath) throws Exception {
        Path src = Paths.get(root.getPath(), srcBucket, srcFilePath);
        Path dst = Paths.get(root.getPath(), bucket, filePath);
        dst.getParent().toFile().mkdirs();
        Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public List<StorageStat> listObjects(String bucket, String prefix) throws Exception {
        File f = Paths.get(root.getPath(), bucket).toFile();
        File[] files = StringUtils.hasText(prefix) ? 
                f.listFiles((dir, name) -> name.startsWith(prefix)) : f.listFiles();
        return Arrays.asList(files).stream()
                .map(file -> new StorageStat(file.getName(), file.length(), 
                        Date.from(Instant.ofEpochMilli(file.lastModified()))))
                .collect(Collectors.toList());
    }

    @Override
    public String getObjectUrl(String bucket, String filePath) throws Exception {
        return Paths.get(root.getPath(), bucket, filePath).toString();
    }

    @Override
    public String getObjectPath(String bucket, String filePath) throws Exception {
        return Paths.get(bucket, filePath).toString();
    }
}
