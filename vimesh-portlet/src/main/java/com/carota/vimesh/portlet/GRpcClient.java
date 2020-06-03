package com.carota.vimesh.portlet;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractBlockingStub;

public abstract class GRpcClient<T extends AbstractBlockingStub<T>> {

    private String serviceName;
    private ManagedChannel channel;
    private T stub;
    private long expireTime;
    
    @SuppressWarnings("unchecked")
    protected final void init(String name, String host, int port, GRpcClientBuilder builder) throws Exception {
        this.serviceName = name;
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub = (T)builder.newStub(this.channel);
    }
    
    protected final void shutdown(long timeout) throws InterruptedException {
        this.channel.shutdown().awaitTermination(timeout, TimeUnit.SECONDS);
    }
    
    protected final T getStub() {
        return this.stub;
    }
    
    protected final String getServiceName() {
        return this.serviceName;
    }
    
    protected final void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
    
    protected final long getExpireTime() {
        return this.expireTime;
    }
}
