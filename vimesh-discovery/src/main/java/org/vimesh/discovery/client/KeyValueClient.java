package org.vimesh.discovery.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.vimesh.discovery.proto.DiscoveryProto.DelRequest;
import org.vimesh.discovery.proto.DiscoveryProto.GetRequest;
import org.vimesh.discovery.proto.DiscoveryProto.GetResponse;
import org.vimesh.discovery.proto.DiscoveryProto.KeysRequest;
import org.vimesh.discovery.proto.DiscoveryProto.KeysResponse;
import org.vimesh.discovery.proto.DiscoveryProto.Result;
import org.vimesh.discovery.proto.DiscoveryProto.SetRequest;
import org.vimesh.discovery.proto.KeyValueServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeyValueClient {

    private final ManagedChannel channel;
    private final KeyValueServiceGrpc.KeyValueServiceBlockingStub kvStub;
    
    public KeyValueClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.kvStub = KeyValueServiceGrpc.newBlockingStub(channel);
    }
    
    public void shutdown(long timeout) throws InterruptedException {
        this.channel.shutdown().awaitTermination(timeout, TimeUnit.SECONDS);
    }
    
    public Map<String, String> get(String key) {
        try {
            GetRequest request = GetRequest.newBuilder().setKey(key).build();
            GetResponse response = kvStub.get(request);
            return response.getDataMap();
        } catch (Exception e) {
            log.error("KeyValueClient get error", e);
            return null;
        }
    }
    
    public List<String> keys(String key) {
        try {
            KeysRequest request = KeysRequest.newBuilder().setKey(key).build();
            KeysResponse response = kvStub.keys(request);
            return response.getKeysList();
        } catch (Exception e) {
            log.error("KeyValueClient keys error", e);
            return null;
        }
    }
    
    public boolean set(String key, String value, String duration) {
        try {
            SetRequest request = SetRequest.newBuilder()
                    .setKey(key)
                    .setValue(value)
                    .setDuration(duration)
                    .build();
            Result response = kvStub.set(request);
            return response.getOk();
        } catch (Exception e) {
            log.error("KeyValueClient set error", e);
            return false;
        }
    }
    
    public boolean del(String key) {
        try {
            DelRequest request = DelRequest.newBuilder().setKey(key).build();
            Result response = kvStub.del(request);
            return response.getOk();
        } catch (Exception e) {
            log.error("KeyValueClient del error", e);
            return false;
        }
    }
}
