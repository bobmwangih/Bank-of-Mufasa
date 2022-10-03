package com.mufasa.gatewayServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	@Autowired
	private TokenRelayGatewayFilterFactory filterFactory;

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p.path("/mufasa/accounts/**")
						.filters(f -> f.filters(filterFactory.apply())
								.rewritePath("/mufasa/accounts/(?<segment>.*)", "/${segment}")
								// .filters(f -> f.rewritePath("/mufasa/accounts/(?<segment>.*)", "/${segment}")
								// .addResponseHeader("X-Response-Time", new Date().toString()))
								.removeRequestHeader("Cookie"))
						.uri("lb://ACCOUNTS"))
				.route(p -> p.path("/mufasa/loans/**").filters(
						f -> f.filters(filterFactory.apply()).rewritePath("/mufasa/loans/(?<segment>.*)", "/${segment}")
						// .filters(f -> f.rewritePath("/mufasa/loans/(?<segment>.*)", "/${segment}")
//								.addResponseHeader("X-Response-Time", new Date().toString()))
								.removeRequestHeader("Cookie"))
						.uri("lb://LOANS"))
				.route(p -> p.path("/mufasa/cards/**").filters(
						f -> f.filters(filterFactory.apply()).rewritePath("/mufasa/cards/(?<segment>.*)", "/${segment}")
						// .filters(f -> f.rewritePath("/mufasa/cards/(?<segment>.*)", "/${segment}")
//								.addResponseHeader("X-Response-Time", new Date().toString()))
								.removeRequestHeader("Cookie"))
						.uri("lb://CARDS"))
				.build();
	}

}
