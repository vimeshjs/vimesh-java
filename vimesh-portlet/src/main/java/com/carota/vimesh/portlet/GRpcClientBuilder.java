package com.carota.vimesh.portlet;

import io.grpc.Channel;

public interface GRpcClientBuilder {

    String getName();
    GRpcClient<?> newClient() throws Exception;
    Object newStub(Channel channel) throws Exception;
}
