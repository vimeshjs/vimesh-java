package org.vimesh.grpc.client;

import io.grpc.Channel;

public interface GRpcClientBuilder {

    String getName();
    GRpcClient<?> newClient() throws Exception;
    Object newStub(Channel channel) throws Exception;
}
