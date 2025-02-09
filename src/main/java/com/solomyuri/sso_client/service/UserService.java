package com.solomyuri.sso_client.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.solomyuri.sso_client.model.ChangeRoleRequest;
import com.solomyuri.sso_client.model.EditUserRequest;
import com.solomyuri.sso_client.model.RegistrationRequest;
import com.solomyuri.sso_client.model.keycloak.UserInfoResponse;

import reactor.core.publisher.Mono;

public interface UserService {

	Mono<ResponseEntity<String>> createUser(RegistrationRequest request);

	Mono<ResponseEntity<List<UserInfoResponse>>> getUserInfo(String username);
	
	Mono<ResponseEntity<String>> deleteUser(String username);

	Mono<ResponseEntity<String>> changeRole(ChangeRoleRequest username);
	
	Mono<ResponseEntity<String>> editUser(String username, EditUserRequest request);
}
