package com.jaab.edelweiss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EdelweissApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdelweissApiGatewayApplication.class, args);
	}

}
