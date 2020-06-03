package com.carota.vimesh.portlet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.reflections.Reflections;

import com.carota.vimesh.grpc.annotation.GRpcService;

import io.grpc.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PortletScanner {
    
    private static final String SCAN_PACKAGE = "com.carota";
    
    private Set<String> services = new HashSet<>();
    private Map<Class<?>, GRpcClientBuilder> clients = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        Reflections reflections = new Reflections(SCAN_PACKAGE);
        
        // scan services
        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(GRpcService.class);
        serviceClasses.forEach(cls -> {
            try {
                Class<?> grpcClass = cls.getSuperclass().getEnclosingClass();
                String serviceName = getServiceName(grpcClass);
                services.add(serviceName);
            } catch (Exception e) {
                log.error(String.format("Scanning gRPC service %s interrupted.", cls.getName()), e);
            }
        });
        
        // scan clients
        @SuppressWarnings("rawtypes")
        Set<Class<? extends GRpcClient>> clientClasses = reflections.getSubTypesOf(GRpcClient.class);
        clientClasses.forEach(cls -> {
            try {
                ParameterizedType pt = (ParameterizedType)cls.getGenericSuperclass();
                Class<?> tClass = (Class<?>)pt.getActualTypeArguments()[0];
                Class<?> grpcClass = tClass.getEnclosingClass();
                
                String serviceName = getServiceName(grpcClass);
                Method newStubMethod = getNewStubMethod(grpcClass);
                GRpcClientBuilder builder = new GRpcClientBuilder() {

                    @Override
                    public String getName() {
                        return serviceName;
                    }

                    @Override
                    public GRpcClient<?> newClient() throws Exception {
                        return cls.newInstance();
                    }

                    @Override
                    public Object newStub(Channel channel) throws Exception {
                        return newStubMethod.invoke(null, channel);
                    }
                
                };
                clients.put(cls, builder);
            } catch (Exception e) {
                log.error(String.format("Scanning gRPC client %s interrupted.", cls.getName()), e);
            }
        });
        
        // normally should not create clients connect to itself
        clients.forEach((cls, builder) -> {
            if (services.contains(builder.getName())) {
                log.warn("gRPC client {} may connect to itself", cls.getName());
            }
        });
    }
    
    private String getServiceName(Class<?> grpcClass) throws Exception {
        Field nameField = grpcClass.getField("SERVICE_NAME");
        return nameField.get(grpcClass).toString();
    }
    
    private Method getNewStubMethod(Class<?> grpcClass) throws Exception {
        return grpcClass.getMethod("newBlockingStub", Channel.class);
    }
}
