package com.solomyuri.sso_client.service;

import org.springframework.http.ResponseEntity;

import com.solomyuri.sso_client.model.ChangeRoleRequest;
import com.solomyuri.sso_client.model.RegistrationRequest;

import reactor.core.publisher.Mono;

public interface UserService {

	Mono<ResponseEntity<String>> createUser(RegistrationRequest request);

	Mono<ResponseEntity<String>> deleteUser(String username);

	Mono<ResponseEntity<String>> changeRole(ChangeRoleRequest username);
}
