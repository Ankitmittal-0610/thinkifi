package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity 
@Configuration 
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private JWTRequestFilter jwtFilter;

	
	@Bean
	public SecurityFilterChain authorizeRequests(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint((request, resp, exc) -> 
					resp.sendError(HttpStatus.UNAUTHORIZED.value(), "Not yet authenticated"))
				.accessDeniedHandler((request, resp, exc) ->
					resp.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied"))
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/users/signin", "/users/signup",
					"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/error").permitAll()
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/events", "/api/events/**").hasAnyRole("USER", "ADMIN")
				.requestMatchers(HttpMethod.POST, "/api/events", "/api/events/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/api/events", "/api/events/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/events", "/api/events/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}

