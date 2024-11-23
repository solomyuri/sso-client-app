package com.solomyuri.sso_client.client;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.solomyuri.sso_client.model.TokenRequest;
import com.solomyuri.sso_client.model.keycloak.CreateUserRequest;
import com.solomyuri.sso_client.model.keycloak.RoleForChange;
import com.solomyuri.sso_client.model.keycloak.TokenResponse;
import com.solomyuri.sso_client.model.keycloak.UserInfoResponse;

import reactor.core.publisher.Mono;

public interface KeycloakClient {

	Mono<TokenResponse> getAccessToken(TokenRequest requestDto);
	Mono<TokenResponse> getAccessToken(String refreshToken);
	Mono<HttpStatus> createUser(CreateUserRequest requestDto);
	Mono<UserInfoResponse> getUser(String username);
	Mono<HttpStatus> deleteUser(UUID userId);
	Mono<HttpStatus> addRoles(UUID userId, List<RoleForChange> roles);
	Mono<HttpStatus> removeRoles(UUID userId, List<RoleForChange> roles);
	
}
