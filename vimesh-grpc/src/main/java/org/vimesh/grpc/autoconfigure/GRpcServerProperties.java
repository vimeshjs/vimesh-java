package org.vimesh.grpc.autoconfigure;

import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.util.SocketUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("grpc")
@Getter @Setter
public class GRpcServerProperties {
	public static final int DEFAULT_GRPC_PORT = 6565;
	public static final int MAX_INBOUND_MESSAGE_SIZE = 4*1024*1024;
    /**
     * gRPC server port
     *
     */
    private Integer port = null;

    private SecurityProperties security ;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private volatile Integer runningPort = null;

    /**
     * Enables the embedded grpc server.
     */
    private boolean enabled = true;


    private int maxInboundMessageSize = MAX_INBOUND_MESSAGE_SIZE;


    /**
     * In process server name.
     * If  the value is not empty, the embedded in-process server will be created and started.
     *
     */
    private String inProcessServerName;

    /**
     * Enables server reflection using <a href="https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md">ProtoReflectionService</a>.
     * Available only from gRPC 1.3 or higher.
     */
    private boolean enableReflection = false;

    /**
     * Number of seconds to wait for preexisting calls to finish before shutting down.
     * A negative value is equivalent to an infinite grace period
     */
    private int shutdownGrace = 0;

    public Integer getRunningPort() {
        if (null == runningPort) {
            synchronized (this) {
                if (null == runningPort) {
                    runningPort = Optional.ofNullable(port)
                            .map(p -> 0 == p ? SocketUtils.findAvailableTcpPort() : p)
                            .orElse(DEFAULT_GRPC_PORT);
                }
            }
        }
        return runningPort;

    }

    @Getter @Setter
    public static class SecurityProperties{
        private Resource certChain;
        private Resource privateKey;
    }
}
