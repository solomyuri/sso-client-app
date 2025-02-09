package com.solomyuri.sso_client.config;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.solomyuri.sso_client.exception.AuthErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

    private final String[] permitList = { "/eapi/sso-client-app/auth/token",
            "/eapi/sso-client-app/auth/refresh", "/eapi/sso-client-app/users" };

    @Bean
    SecurityWebFilterChain filterChain(ServerHttpSecurity http, AuthErrorHandler entryPoint) {

	http.oauth2ResourceServer(
	        oAuth2 -> oAuth2.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter())));
	http.authorizeExchange(auth -> auth.pathMatchers(HttpMethod.POST, permitList)
	        .permitAll()
	        .anyExchange()
	        .hasAuthority("sso-client-app"));
	http.csrf(csrf -> csrf.disable());
	http.exceptionHandling(
	        exception -> exception.authenticationEntryPoint(entryPoint).accessDeniedHandler(entryPoint));
	return http.build();
    }

    @Bean
    ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {

	var jwtAuthenticationConverter = new JwtAuthenticationConverter();
	jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);

	return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {

	return Optional.ofNullable(jwt.getClaimAsMap("resource_access"))
	        .map(Map::values)
	        .stream()
	        .flatMap(Collection::stream)
	        .map(clientAccess -> ((Map<String, Collection<String>>) clientAccess).get("roles"))
	        .filter(Objects::nonNull)
	        .flatMap(Collection::stream)
	        .map(SimpleGrantedAuthority::new)
	        .collect(Collectors.toList());
    }
}
