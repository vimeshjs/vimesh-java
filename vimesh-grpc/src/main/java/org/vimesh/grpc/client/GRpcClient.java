package org.vimesh.grpc.client;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractBlockingStub;

public abstract class GRpcClient<T extends AbstractBlockingStub<T>> {

    private String serviceName;
    private ManagedChannel channel;
    private T stub;
    private Instant expireTime;
    
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
    
    public final void setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
    }
    
    public final boolean isExpired(Instant now) {
        return this.expireTime != null ? now.isAfter(this.expireTime) : false;
    }
}
