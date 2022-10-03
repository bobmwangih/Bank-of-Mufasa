package com.mufasa.gatewayServer.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity http) {
		//http.authorizeExchange(exchanges -> exchanges.pathMatchers("/mufasa/accounts/**").hasRole("ACCOUNTS")
		http.authorizeExchange(exchanges -> exchanges.pathMatchers("/mufasa/accounts/**").authenticated()
				.pathMatchers("/mufasa/cards/**").authenticated().pathMatchers("/mufasa/loans/**").permitAll())
				//.oauth2ResourceServer().jwt().jwtAuthenticationConverter(grantedAuthoritiesExtractor());
		.oauth2Login(Customizer.withDefaults());
		http.csrf().disable();
		return http.build();
	}

//	Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
//		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
//		return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
//	}
}
