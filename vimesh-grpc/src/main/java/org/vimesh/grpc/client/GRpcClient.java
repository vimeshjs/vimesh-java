package org.vimesh.grpc.client;

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
    public final void init(String name, String host, int port, GRpcClientBuilder builder) throws Exception {
        this.serviceName = name;
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub = (T)builder.newStub(this.channel);
    }
    
    public final void shutdown(long timeout) throws InterruptedException {
        this.channel.shutdown().awaitTermination(timeout, TimeUnit.SECONDS);
    }
    
    protected final T getStub() {
        return this.stub;
    }
    
    public final String getServiceName() {
        return this.serviceName;
    }
    
    public final void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
    
    public final long getExpireTime() {
        return this.expireTime;
    }
}
