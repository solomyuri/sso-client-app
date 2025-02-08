package com.solomyuri.sso_client.client;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.solomyuri.sso_client.exception.ApplicationException;
import com.solomyuri.sso_client.model.EditUserRequest;
import com.solomyuri.sso_client.model.TokenRequest;
import com.solomyuri.sso_client.model.keycloak.CreateUserRequest;
import com.solomyuri.sso_client.model.keycloak.KeycloakError;
import com.solomyuri.sso_client.model.keycloak.RoleForChange;
import com.solomyuri.sso_client.model.keycloak.TokenResponse;
import com.solomyuri.sso_client.model.keycloak.UserInfoResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class KeycloakClientImpl implements KeycloakClient {

    private final WebClient webClientAuth;
    private final WebClient webClient;
    private final String KEYCLOAK_CLIENT_ID;
    private final String KEYCLOAK_CLIENT_SECRET;
    private final String TOKEN_PATH;
    private final String USERS_PATH;
    private final String GRANT_TYPE = "grant_type";
    private final String REFRESH_TOKEN = "refresh_token";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String CLIENT_ID = "client_id";
    private final String CLIENT_SECRET = "client_secret";
    private final String CLIENT_ERROR_MESSAGE_TEMPLATE = "{} {} STATUS: {}";

    public KeycloakClientImpl(@Qualifier("WebClientAuth") WebClient webClientAuth,
            @Qualifier("WebClient") WebClient webClient, @Value("${keycloak.client.id}") String clientId,
            @Value("${keycloak.client.secret}") String clientSecret, @Value("${keycloak.token.path}") String tokenPath,
            @Value("${keycloak.users.path}") String usersPath) {
	this.webClientAuth = webClientAuth;
	this.webClient = webClient;
	this.KEYCLOAK_CLIENT_ID = clientId;
	this.KEYCLOAK_CLIENT_SECRET = clientSecret;
	this.TOKEN_PATH = tokenPath;
	this.USERS_PATH = usersPath;
    }

    @Override
    public Mono<TokenResponse> getAccessToken(TokenRequest request) {

	return webClientAuth.post()
	        .uri(TOKEN_PATH)
	        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	        .body(BodyInserters.fromFormData(GRANT_TYPE, "password")
	                .with(USERNAME, request.getUsername())
	                .with(PASSWORD, request.getPassword())
	                .with(CLIENT_ID, KEYCLOAK_CLIENT_ID)
	                .with(CLIENT_SECRET, KEYCLOAK_CLIENT_SECRET))
	        .exchangeToMono(response -> {
	            if (response.statusCode().is2xxSuccessful())
		        return response.bodyToMono(TokenResponse.class);
	            else
		        return handleErrorResponse(response, "POST", TOKEN_PATH).then(Mono.empty());
	        });
    }

    @Override
    public Mono<TokenResponse> getAccessToken(String refreshToken) {

	return webClientAuth.post()
	        .uri(TOKEN_PATH)
	        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	        .body(BodyInserters.fromFormData(GRANT_TYPE, REFRESH_TOKEN)
	                .with(REFRESH_TOKEN, refreshToken)
	                .with(CLIENT_ID, KEYCLOAK_CLIENT_ID)
	                .with(CLIENT_SECRET, KEYCLOAK_CLIENT_SECRET))
	        .exchangeToMono(response -> {
	            if (response.statusCode().is2xxSuccessful())
		        return response.bodyToMono(TokenResponse.class);
	            else
		        return handleErrorResponse(response, "POST", TOKEN_PATH).then(Mono.empty());
	        });
    }

    @Override
    public Mono<HttpStatus> createUser(CreateUserRequest requestDto) {

	return webClient.post().uri(USERS_PATH).bodyValue(requestDto).exchangeToMono(response -> {
	    if (response.statusCode().is2xxSuccessful())
		return Mono.just(HttpStatus.CREATED);
	    else
		return handleErrorResponse(response, "POST", USERS_PATH).then(Mono.empty());
	});
    }

    @Override
    public Mono<UserInfoResponse> getUser(String username) {

	return webClient.get()
	        .uri(uriBuilder -> uriBuilder.path(USERS_PATH).queryParam(USERNAME, username).build())
	        .exchangeToMono(response -> {
	            if (response.statusCode().is2xxSuccessful())
		        return response.bodyToFlux(UserInfoResponse.class)
		                .next()
		                .switchIfEmpty(
		                        Mono.error(new ApplicationException("user not found", HttpStatus.NOT_FOUND)));
	            else
		        return handleErrorResponse(response, "GET", USERS_PATH).then(Mono.empty());
	        });
    }

    @Override
    public Mono<HttpStatus> deleteUser(UUID userId) {

	return webClient.delete()
	        .uri(uriBuilder -> uriBuilder.path(USERS_PATH).pathSegment(userId.toString()).build())
	        .exchangeToMono(response -> {
	            if (response.statusCode().is2xxSuccessful())
		        return Mono.just(HttpStatus.NO_CONTENT);
	            else
		        return handleErrorResponse(response, "DELETE", USERS_PATH).then(Mono.empty());
	        });
    }

    @Override
    public Mono<HttpStatus> addRoles(UUID userId, String clientId, List<RoleForChange> roles) {

	return webClient.post()
	        .uri(uriBuilder -> uriBuilder.path(USERS_PATH)
	                .pathSegment(userId.toString(), "role-mappings", "clients", clientId)
	                .build())
	        .bodyValue(roles)
	        .exchangeToMono(response -> {
	            if (response.statusCode().is2xxSuccessful())
		        return Mono.just(HttpStatus.OK);
	            else
		        return handleErrorResponse(response, "POST", USERS_PATH + "/roles").then(Mono.empty());
	        });
    }

    @Override
    public Mono<HttpStatus> removeRoles(UUID userId, String clientId, List<RoleForChange> roles) {

	return webClient.method(HttpMethod.DELETE)
	        .uri(uriBuilder -> uriBuilder.path(USERS_PATH)
	                .pathSegment(userId.toString(), "role-mappings", "clients", clientId)
	                .build())
	        .bodyValue(roles)
	        .exchangeToMono(response -> {
	            if (response.statusCode().is2xxSuccessful())
		        return Mono.just(HttpStatus.OK);
	            else
		        return handleErrorResponse(response, "DELETE", USERS_PATH + "/roles").then(Mono.empty());
	        });
    }

    private Mono<Void> handleErrorResponse(ClientResponse response, String method, String path) {

	log.error(CLIENT_ERROR_MESSAGE_TEMPLATE, method, path, response.statusCode().toString());
	HttpStatus status = HttpStatus.valueOf(response.statusCode().value());

	return response.bodyToMono(KeycloakError.class).flatMap(error -> {
	    StringBuilder message = new StringBuilder();

	    if (Objects.nonNull(error.getError()))
		message.append(error.getError());

	    if (Objects.nonNull(error.getErrorDescription())) {
		if (!message.isEmpty())
		    message.append("; ");
		message.append(error.getErrorDescription());
	    }

	    if (Objects.nonNull(error.getErrorMessage())) {
		if (!message.isEmpty())
		    message.append("; ");
		message.append(error.getErrorMessage());
	    }

	    return Mono.error(new ApplicationException(message.toString(), status));
	});

    }

    @Override
    public Mono<HttpStatus> editUser(UUID userId, EditUserRequest request) {
	return webClient.put()
	        .uri(uriBuilder -> uriBuilder.path(USERS_PATH)
	                .pathSegment(userId.toString())
	                .build())
	        .bodyValue(request)
	        .exchangeToMono(response -> {
	            if (response.statusCode().is2xxSuccessful())
		        return Mono.just(HttpStatus.OK);
	            else
		        return handleErrorResponse(response, "PUT", USERS_PATH).then(Mono.empty());
	        });
    }

}
