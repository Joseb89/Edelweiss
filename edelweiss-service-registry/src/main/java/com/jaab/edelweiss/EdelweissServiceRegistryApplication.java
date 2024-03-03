package com.jaab.edelweiss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EdelweissServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdelweissServiceRegistryApplication.class, args);
	}

}
