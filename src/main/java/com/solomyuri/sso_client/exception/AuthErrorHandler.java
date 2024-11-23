package com.solomyuri.sso_client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solomyuri.sso_client.model.ErrorResponse;

import reactor.core.publisher.Mono;

@Component
public class AuthErrorHandler implements ServerAuthenticationEntryPoint, ServerAccessDeniedHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {

		return handleError(exchange, ex);
	}

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {

		return handleError(exchange, denied);
	}

	private Mono<Void> handleError(ServerWebExchange exchange, Exception ex) {

		int code = 401;
		HttpStatus status = HttpStatus.UNAUTHORIZED;

		if (ex instanceof AccessDeniedException) {
			code = 403;
			status = HttpStatus.FORBIDDEN;
		}

		ErrorResponse errorResponse = new ErrorResponse(code, ex.getMessage());
		exchange.getResponse().setStatusCode(status);
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

		try {
			byte[] responseBody = objectMapper.writeValueAsBytes(errorResponse);
			return exchange.getResponse()
					.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBody)));
		} catch (Exception e) {
			return Mono.error(e);
		}
	}

}