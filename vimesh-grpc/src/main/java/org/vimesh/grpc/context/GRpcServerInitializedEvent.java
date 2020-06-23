package org.vimesh.grpc.context;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

import io.grpc.Server;

public class GRpcServerInitializedEvent extends ApplicationEvent {
    private final Server server;

    public GRpcServerInitializedEvent(ApplicationContext context,Server server) {
        super(context);
        this.server = server;
    }

    public ApplicationContext getApplicationContext(){
        return (ApplicationContext) getSource();
    }

    public Server getServer(){
        return server;
    }
}
