package org.vimesh.discovery.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.28.1)",
    comments = "Source: discovery/discovery.proto")
public final class KeyValueServiceGrpc {

  private KeyValueServiceGrpc() {}

  public static final String SERVICE_NAME = "KeyValueService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.GetRequest,
      org.vimesh.discovery.proto.DiscoveryProto.GetResponse> getGetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "get",
      requestType = org.vimesh.discovery.proto.DiscoveryProto.GetRequest.class,
      responseType = org.vimesh.discovery.proto.DiscoveryProto.GetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.GetRequest,
      org.vimesh.discovery.proto.DiscoveryProto.GetResponse> getGetMethod() {
    io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.GetRequest, org.vimesh.discovery.proto.DiscoveryProto.GetResponse> getGetMethod;
    if ((getGetMethod = KeyValueServiceGrpc.getGetMethod) == null) {
      synchronized (KeyValueServiceGrpc.class) {
        if ((getGetMethod = KeyValueServiceGrpc.getGetMethod) == null) {
          KeyValueServiceGrpc.getGetMethod = getGetMethod =
              io.grpc.MethodDescriptor.<org.vimesh.discovery.proto.DiscoveryProto.GetRequest, org.vimesh.discovery.proto.DiscoveryProto.GetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "get"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vimesh.discovery.proto.DiscoveryProto.GetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vimesh.discovery.proto.DiscoveryProto.GetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KeyValueServiceMethodDescriptorSupplier("get"))
              .build();
        }
      }
    }
    return getGetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.KeysRequest,
      org.vimesh.discovery.proto.DiscoveryProto.KeysResponse> getKeysMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "keys",
      requestType = org.vimesh.discovery.proto.DiscoveryProto.KeysRequest.class,
      responseType = org.vimesh.discovery.proto.DiscoveryProto.KeysResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.KeysRequest,
      org.vimesh.discovery.proto.DiscoveryProto.KeysResponse> getKeysMethod() {
    io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.KeysRequest, org.vimesh.discovery.proto.DiscoveryProto.KeysResponse> getKeysMethod;
    if ((getKeysMethod = KeyValueServiceGrpc.getKeysMethod) == null) {
      synchronized (KeyValueServiceGrpc.class) {
        if ((getKeysMethod = KeyValueServiceGrpc.getKeysMethod) == null) {
          KeyValueServiceGrpc.getKeysMethod = getKeysMethod =
              io.grpc.MethodDescriptor.<org.vimesh.discovery.proto.DiscoveryProto.KeysRequest, org.vimesh.discovery.proto.DiscoveryProto.KeysResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "keys"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vimesh.discovery.proto.DiscoveryProto.KeysRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vimesh.discovery.proto.DiscoveryProto.KeysResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KeyValueServiceMethodDescriptorSupplier("keys"))
              .build();
        }
      }
    }
    return getKeysMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.SetRequest,
      org.vimesh.discovery.proto.DiscoveryProto.Result> getSetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "set",
      requestType = org.vimesh.discovery.proto.DiscoveryProto.SetRequest.class,
      responseType = org.vimesh.discovery.proto.DiscoveryProto.Result.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.SetRequest,
      org.vimesh.discovery.proto.DiscoveryProto.Result> getSetMethod() {
    io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.SetRequest, org.vimesh.discovery.proto.DiscoveryProto.Result> getSetMethod;
    if ((getSetMethod = KeyValueServiceGrpc.getSetMethod) == null) {
      synchronized (KeyValueServiceGrpc.class) {
        if ((getSetMethod = KeyValueServiceGrpc.getSetMethod) == null) {
          KeyValueServiceGrpc.getSetMethod = getSetMethod =
              io.grpc.MethodDescriptor.<org.vimesh.discovery.proto.DiscoveryProto.SetRequest, org.vimesh.discovery.proto.DiscoveryProto.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "set"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vimesh.discovery.proto.DiscoveryProto.SetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vimesh.discovery.proto.DiscoveryProto.Result.getDefaultInstance()))
              .setSchemaDescriptor(new KeyValueServiceMethodDescriptorSupplier("set"))
              .build();
        }
      }
    }
    return getSetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.DelRequest,
      org.vimesh.discovery.proto.DiscoveryProto.Result> getDelMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "del",
      requestType = org.vimesh.discovery.proto.DiscoveryProto.DelRequest.class,
      responseType = org.vimesh.discovery.proto.DiscoveryProto.Result.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.DelRequest,
      org.vimesh.discovery.proto.DiscoveryProto.Result> getDelMethod() {
    io.grpc.MethodDescriptor<org.vimesh.discovery.proto.DiscoveryProto.DelRequest, org.vimesh.discovery.proto.DiscoveryProto.Result> getDelMethod;
    if ((getDelMethod = KeyValueServiceGrpc.getDelMethod) == null) {
      synchronized (KeyValueServiceGrpc.class) {
        if ((getDelMethod = KeyValueServiceGrpc.getDelMethod) == null) {
          KeyValueServiceGrpc.getDelMethod = getDelMethod =
              io.grpc.MethodDescriptor.<org.vimesh.discovery.proto.DiscoveryProto.DelRequest, org.vimesh.discovery.proto.DiscoveryProto.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "del"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vimesh.discovery.proto.DiscoveryProto.DelRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vimesh.discovery.proto.DiscoveryProto.Result.getDefaultInstance()))
              .setSchemaDescriptor(new KeyValueServiceMethodDescriptorSupplier("del"))
              .build();
        }
      }
    }
    return getDelMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static KeyValueServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KeyValueServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KeyValueServiceStub>() {
        @java.lang.Override
        public KeyValueServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KeyValueServiceStub(channel, callOptions);
        }
      };
    return KeyValueServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static KeyValueServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KeyValueServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KeyValueServiceBlockingStub>() {
        @java.lang.Override
        public KeyValueServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KeyValueServiceBlockingStub(channel, callOptions);
        }
      };
    return KeyValueServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static KeyValueServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KeyValueServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KeyValueServiceFutureStub>() {
        @java.lang.Override
        public KeyValueServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KeyValueServiceFutureStub(channel, callOptions);
        }
      };
    return KeyValueServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class KeyValueServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void get(org.vimesh.discovery.proto.DiscoveryProto.GetRequest request,
        io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.GetResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMethod(), responseObserver);
    }

    /**
     */
    public void keys(org.vimesh.discovery.proto.DiscoveryProto.KeysRequest request,
        io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.KeysResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getKeysMethod(), responseObserver);
    }

    /**
     */
    public void set(org.vimesh.discovery.proto.DiscoveryProto.SetRequest request,
        io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.Result> responseObserver) {
      asyncUnimplementedUnaryCall(getSetMethod(), responseObserver);
    }

    /**
     */
    public void del(org.vimesh.discovery.proto.DiscoveryProto.DelRequest request,
        io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.Result> responseObserver) {
      asyncUnimplementedUnaryCall(getDelMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.vimesh.discovery.proto.DiscoveryProto.GetRequest,
                org.vimesh.discovery.proto.DiscoveryProto.GetResponse>(
                  this, METHODID_GET)))
          .addMethod(
            getKeysMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.vimesh.discovery.proto.DiscoveryProto.KeysRequest,
                org.vimesh.discovery.proto.DiscoveryProto.KeysResponse>(
                  this, METHODID_KEYS)))
          .addMethod(
            getSetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.vimesh.discovery.proto.DiscoveryProto.SetRequest,
                org.vimesh.discovery.proto.DiscoveryProto.Result>(
                  this, METHODID_SET)))
          .addMethod(
            getDelMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.vimesh.discovery.proto.DiscoveryProto.DelRequest,
                org.vimesh.discovery.proto.DiscoveryProto.Result>(
                  this, METHODID_DEL)))
          .build();
    }
  }

  /**
   */
  public static final class KeyValueServiceStub extends io.grpc.stub.AbstractAsyncStub<KeyValueServiceStub> {
    private KeyValueServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KeyValueServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KeyValueServiceStub(channel, callOptions);
    }

    /**
     */
    public void get(org.vimesh.discovery.proto.DiscoveryProto.GetRequest request,
        io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.GetResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void keys(org.vimesh.discovery.proto.DiscoveryProto.KeysRequest request,
        io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.KeysResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getKeysMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void set(org.vimesh.discovery.proto.DiscoveryProto.SetRequest request,
        io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.Result> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void del(org.vimesh.discovery.proto.DiscoveryProto.DelRequest request,
        io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.Result> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDelMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class KeyValueServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<KeyValueServiceBlockingStub> {
    private KeyValueServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KeyValueServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KeyValueServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.vimesh.discovery.proto.DiscoveryProto.GetResponse get(org.vimesh.discovery.proto.DiscoveryProto.GetRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.vimesh.discovery.proto.DiscoveryProto.KeysResponse keys(org.vimesh.discovery.proto.DiscoveryProto.KeysRequest request) {
      return blockingUnaryCall(
          getChannel(), getKeysMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.vimesh.discovery.proto.DiscoveryProto.Result set(org.vimesh.discovery.proto.DiscoveryProto.SetRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.vimesh.discovery.proto.DiscoveryProto.Result del(org.vimesh.discovery.proto.DiscoveryProto.DelRequest request) {
      return blockingUnaryCall(
          getChannel(), getDelMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class KeyValueServiceFutureStub extends io.grpc.stub.AbstractFutureStub<KeyValueServiceFutureStub> {
    private KeyValueServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KeyValueServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KeyValueServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.vimesh.discovery.proto.DiscoveryProto.GetResponse> get(
        org.vimesh.discovery.proto.DiscoveryProto.GetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.vimesh.discovery.proto.DiscoveryProto.KeysResponse> keys(
        org.vimesh.discovery.proto.DiscoveryProto.KeysRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getKeysMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.vimesh.discovery.proto.DiscoveryProto.Result> set(
        org.vimesh.discovery.proto.DiscoveryProto.SetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.vimesh.discovery.proto.DiscoveryProto.Result> del(
        org.vimesh.discovery.proto.DiscoveryProto.DelRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDelMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET = 0;
  private static final int METHODID_KEYS = 1;
  private static final int METHODID_SET = 2;
  private static final int METHODID_DEL = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final KeyValueServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(KeyValueServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET:
          serviceImpl.get((org.vimesh.discovery.proto.DiscoveryProto.GetRequest) request,
              (io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.GetResponse>) responseObserver);
          break;
        case METHODID_KEYS:
          serviceImpl.keys((org.vimesh.discovery.proto.DiscoveryProto.KeysRequest) request,
              (io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.KeysResponse>) responseObserver);
          break;
        case METHODID_SET:
          serviceImpl.set((org.vimesh.discovery.proto.DiscoveryProto.SetRequest) request,
              (io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.Result>) responseObserver);
          break;
        case METHODID_DEL:
          serviceImpl.del((org.vimesh.discovery.proto.DiscoveryProto.DelRequest) request,
              (io.grpc.stub.StreamObserver<org.vimesh.discovery.proto.DiscoveryProto.Result>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class KeyValueServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    KeyValueServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.vimesh.discovery.proto.DiscoveryProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("KeyValueService");
    }
  }

  private static final class KeyValueServiceFileDescriptorSupplier
      extends KeyValueServiceBaseDescriptorSupplier {
    KeyValueServiceFileDescriptorSupplier() {}
  }

  private static final class KeyValueServiceMethodDescriptorSupplier
      extends KeyValueServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    KeyValueServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (KeyValueServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new KeyValueServiceFileDescriptorSupplier())
              .addMethod(getGetMethod())
              .addMethod(getKeysMethod())
              .addMethod(getSetMethod())
              .addMethod(getDelMethod())
              .build();
        }
      }
    }
    return result;
  }
}
