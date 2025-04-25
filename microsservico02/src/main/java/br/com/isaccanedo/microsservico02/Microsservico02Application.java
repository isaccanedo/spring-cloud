package br.com.isaccanedo.microsservico02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class Microsservico02Application {

	public static void main(String[] args) {
		SpringApplication.run(Microsservico02Application.class, args);
	}

}
