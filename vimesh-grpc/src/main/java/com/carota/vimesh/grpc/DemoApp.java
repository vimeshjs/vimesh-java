package com.carota.vimesh.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.carota.vimesh.grpc.annotation.GRpcService;
import com.carota.vimesh.grpc.interceptor.NotSpringBeanInterceptor;
import com.carota.vimesh.grpc.service.GreeterService;
import com.carota.vimesh.grpc.service.HelloWordService;

import io.grpc.examples.CalculatorGrpc;
import io.grpc.examples.CalculatorOuterClass;
import io.grpc.stub.StreamObserver;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class DemoApp {
	@Bean
	public GreeterService greeterService() {
		return new GreeterService();
	}

	@Bean
	public HelloWordService helloWordService() {
		return new HelloWordService();
	}

	@GRpcService(interceptors = NotSpringBeanInterceptor.class)
	public static class CalculatorService extends CalculatorGrpc.CalculatorImplBase {
		@Override
		public void calculate(CalculatorOuterClass.CalculatorRequest request,
				StreamObserver<CalculatorOuterClass.CalculatorResponse> responseObserver) {
			CalculatorOuterClass.CalculatorResponse.Builder resultBuilder = CalculatorOuterClass.CalculatorResponse
					.newBuilder();
			switch (request.getOperation()) {
			case ADD:
				resultBuilder.setResult(request.getNumber1() + request.getNumber2());
				break;
			case SUBTRACT:
				resultBuilder.setResult(request.getNumber1() - request.getNumber2());
				break;
			case MULTIPLY:
				resultBuilder.setResult(request.getNumber1() * request.getNumber2());
				break;
			case DIVIDE:
				resultBuilder.setResult(request.getNumber1() / request.getNumber2());
				break;
			case UNRECOGNIZED:
				break;
			}
			responseObserver.onNext(resultBuilder.build());
			responseObserver.onCompleted();

		}

	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApp.class, args);
	}
}
