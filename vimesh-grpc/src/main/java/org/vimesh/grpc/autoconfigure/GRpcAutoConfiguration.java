package org.vimesh.grpc.autoconfigure;


import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.vimesh.grpc.GRpcServerBuilderConfigurer;
import org.vimesh.grpc.GRpcServerRunner;
import org.vimesh.grpc.annotation.GRpcService;

import io.grpc.ServerBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.services.HealthStatusManager;
import lombok.extern.slf4j.Slf4j;

@AutoConfigureOrder
@ConditionalOnBean(annotation = GRpcService.class)
@EnableConfigurationProperties (GRpcServerProperties.class)
@Slf4j
public class GRpcAutoConfiguration {

    @Autowired
    private GRpcServerProperties grpcServerProperties;

    @Bean
    @ConditionalOnProperty(value = "grpc.enabled", havingValue = "true", matchIfMissing = true)
    public GRpcServerRunner grpcServerRunner(@Qualifier("grpcInternalConfigurator") Consumer<ServerBuilder<?>> configurator) {
        return new GRpcServerRunner(configurator, ServerBuilder.forPort(grpcServerProperties.getRunningPort()),grpcServerProperties.getMaxInboundMessageSize());
    }

    @Bean
    @ConditionalOnExpression("#{environment.getProperty('grpc.inProcessServerName','')!=''}")
    public GRpcServerRunner grpcInprocessServerRunner(@Qualifier("grpcInternalConfigurator") Consumer<ServerBuilder<?>> configurator){
        return new GRpcServerRunner(configurator, InProcessServerBuilder.forName(grpcServerProperties.getInProcessServerName()),grpcServerProperties.getMaxInboundMessageSize());
    }

    @Bean
    public HealthStatusManager healthStatusManager() {
        return new HealthStatusManager();
    }

    @Bean
    @ConditionalOnMissingBean(  GRpcServerBuilderConfigurer.class)
    public GRpcServerBuilderConfigurer serverBuilderConfigurer(){
        return new GRpcServerBuilderConfigurer();
    }

    @Bean(name = "grpcInternalConfigurator")
    public Consumer<ServerBuilder<?>> configurator(GRpcServerBuilderConfigurer configurer){
        return serverBuilder -> {
            if(grpcServerProperties.isEnabled()){
                Optional.ofNullable(grpcServerProperties.getSecurity())
                        .ifPresent(s->{
                            boolean setupSecurity = Optional.ofNullable(s.getCertChain()).isPresent();
                            if(setupSecurity != Optional.ofNullable(s.getPrivateKey()).isPresent() ){
                                throw  new BeanCreationException("Both  gRPC  TLS 'certChain' and 'privateKey' should be configured. One of them is null. ");
                            }
                            if(setupSecurity) {
                                try {
                                    serverBuilder.useTransportSecurity(s.getCertChain().getInputStream(),
                                            s.getPrivateKey().getInputStream()
                                    );
                                } catch (IOException e) {
                                    throw new BeanCreationException("Failed to setup security", e);
                                }
                            }
                        });
            }
            configurer.configure(serverBuilder);
        };
    }

}

