package com.lti.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	public static final String ADMIN = "admin";
	public static final String USER = "user";

	@Autowired
	private JwtAuthConverter jwtAuthConverter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests()
				// free access
				.requestMatchers(HttpMethod.GET, "/hello").permitAll()
				// free access
				.requestMatchers(HttpMethod.GET, "/swagger-ui/index.html").permitAll()
				// admin access
				.requestMatchers(HttpMethod.GET, "/emp", "/emp/**").hasRole(ADMIN)
				// admin and user access
				.requestMatchers(HttpMethod.GET, "/dept", "/dept/**").hasAnyRole(ADMIN, USER)
				//
				.anyRequest().authenticated();

		http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthConverter);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return http.build();
	}

}