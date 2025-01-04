package com.example.urlshortener;

import com.example.urlshortener.service.RequestProducerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class UrlShortenerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(UrlShortenerApplication.class, args);
		RequestProducerService requestProducerService = context.getBean(RequestProducerService.class);

		long totalTime = requestProducerService.simulateHighTraffic();

		System.out.println("Total time taken for all requests: " + totalTime + " ms");

		context.close();
	}
}