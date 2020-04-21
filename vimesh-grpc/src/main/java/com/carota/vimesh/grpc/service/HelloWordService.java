package com.carota.vimesh.grpc.service;

import com.carota.vimesh.grpc.annotation.GRpcService;
import com.carota.vimesh.helloworld.HelloWorldProto;
import com.carota.vimesh.helloworld.HelloWorldProto.HelloRequest;
import com.carota.vimesh.helloworld.HellowordServiceGrpc.HellowordServiceImplBase;

import io.grpc.stub.StreamObserver;
@GRpcService
public class HelloWordService extends HellowordServiceImplBase {
	@Override

	public void sayHello(HelloRequest request, StreamObserver responseObserver) {

		String name = request.getName();

		System.out.println(name);

		HelloWorldProto.HelloReply reply = HelloWorldProto.HelloReply.newBuilder().setMessage(("Hello: " + request.getName()))
				.build();

		responseObserver.onNext(reply);

		responseObserver.onCompleted();

	}
}
