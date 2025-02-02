package com.solomyuri.sso_client.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solomyuri.sso_client.client.KeycloakClient;
import com.solomyuri.sso_client.model.RefreshTokenRequest;
import com.solomyuri.sso_client.model.TokenRequest;
import com.solomyuri.sso_client.model.keycloak.TokenResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

	private final KeycloakClient keycloakClient;

	@PostMapping(path = "/token", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Mono<ResponseEntity<TokenResponse>> getAccessTokenByCredentials(TokenRequest requestDto) {

		return keycloakClient.getAccessToken(requestDto).map(dto -> ResponseEntity.ok(dto));
	}

	@PostMapping(path = "/refresh", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Mono<ResponseEntity<TokenResponse>> getAccessTokenByRefreshToken(RefreshTokenRequest requestDto) {

		return keycloakClient.getAccessToken(requestDto.getRefresh_token()).map(dto -> ResponseEntity.ok(dto));
	}

}
