package org.vimesh.storage.impl;

import java.io.InputStream;
import java.util.List;

import org.vimesh.storage.Storage;
import org.vimesh.storage.StorageStat;

public class LocalStorage implements Storage {

    @Override
    public List<String> listBuckets() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasBucket(String bucket) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void createBucket(String bucket) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void ensureBucket(String bucket) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteBucket(String bucket) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void putObject(String bucket, String filePath, String localFile) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void putObject(String bucket, String filePath, InputStream stream) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void putObject(String bucket, String filePath, byte[] data) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void getObject(String bucket, String filePath, String localFile) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public InputStream getObject(String bucket, String filePath) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getObject(String bucket, String filePath, long offset, long length) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteObject(String bucket, String filePath) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public StorageStat statObject(String bucket, String filePath) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void copyObject(String bucket, String filePath, String srcBucket, String srcFilePath) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<StorageStat> listObjects(String bucket, String prefix) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getObjectUrl(String bucket, String filePath) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
