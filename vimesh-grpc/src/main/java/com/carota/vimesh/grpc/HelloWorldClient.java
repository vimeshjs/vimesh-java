package com.carota.vimesh.grpc;

import java.util.concurrent.TimeUnit;

import com.carota.vimesh.helloworld.HelloWorldProto;
import com.carota.vimesh.helloworld.HellowordServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class HelloWorldClient {
	private final ManagedChannel channel;

	private final HellowordServiceGrpc.HellowordServiceBlockingStub blockingStub;

	public HelloWorldClient(String host, int port) {

		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

		blockingStub = HellowordServiceGrpc.newBlockingStub(channel);

	}

	public void shutdown() throws InterruptedException {

		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);

	}

	public void sayHello(String name) {

		HelloWorldProto.HelloRequest request = HelloWorldProto.HelloRequest.newBuilder().setName(name).build();

		HelloWorldProto.HelloReply response = blockingStub.sayHello(request);

		System.out.println(response.getMessage());

	}

	public static void main(String[] args) throws InterruptedException {

		HelloWorldClient client = new HelloWorldClient("127.0.0.1", 6565);

		for (int i = 0; i < 5; i++) {

			client.sayHello("world:" + i);

		}

		client.shutdown();

	}

}
