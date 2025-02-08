package com.solomyuri.sso_client.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.solomyuri.sso_client.client.KeycloakClient;
import com.solomyuri.sso_client.enums.GameRole;
import com.solomyuri.sso_client.exception.ApplicationException;
import com.solomyuri.sso_client.mapper.RoleForChangeMapper;
import com.solomyuri.sso_client.model.ChangeRoleRequest;
import com.solomyuri.sso_client.model.EditUserRequest;
import com.solomyuri.sso_client.model.RegistrationRequest;
import com.solomyuri.sso_client.model.keycloak.CreateUserRequest;
import com.solomyuri.sso_client.model.keycloak.RoleForChange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserServiceImpl implements UserService {

	private final KeycloakClient keycloakClient;
	private final RoleForChangeMapper roleMapper;
	private final String gameClientId;
	
	public UserServiceImpl(KeycloakClient keycloakClient, RoleForChangeMapper roleMapper, 
	                       @Value("${game.service.client.id}") String gameClientId) {
	    this.keycloakClient = keycloakClient;
	    this.roleMapper = roleMapper;
	    this.gameClientId = gameClientId;
	}
	

	@Override
	public Mono<ResponseEntity<String>> createUser(RegistrationRequest request) {

	    return keycloakClient.createUser(new CreateUserRequest(request.getUsername(), request.getPassword()))
	            .flatMap(status -> keycloakClient.getUser(request.getUsername())
	                    .flatMap(userInfo -> keycloakClient
	                            .addRoles(userInfo.getId(), gameClientId,
	                                    List.of(roleMapper.toRoleForChange(GameRole.USER)))
	                            .thenReturn(ResponseEntity.status(HttpStatus.OK)
	                                    .body("CREATED"))));
	}

	@Override
	public Mono<ResponseEntity<String>> deleteUser(String username) {

		return keycloakClient.getUser(username)
				.flatMap(userInfo -> keycloakClient.deleteUser(userInfo.getId())
						.map(status -> ResponseEntity.status(status).body("DELETED")));
	}

	@Override
	public Mono<ResponseEntity<String>> changeRole(ChangeRoleRequest request) {

		List<RoleForChange> roles = request.getRoles().stream().map(roleMapper::toRoleForChange).toList();

		return keycloakClient.getUser(request.getUsername()).flatMap(userInfo -> {
			return switch (request.getAction()) {
			case ADD -> keycloakClient.addRoles(userInfo.getId(), gameClientId, roles)
					.thenReturn(ResponseEntity.status(HttpStatus.OK).body("ROLES ADDED"));
			case RM -> keycloakClient.removeRoles(userInfo.getId(), gameClientId, roles)
					.thenReturn(ResponseEntity.status(HttpStatus.OK).body("ROLES REMOVED"));
			default -> Mono.error(new ApplicationException("invalid action", HttpStatus.BAD_REQUEST));
			};
		});
	}

	@Override
	public Mono<ResponseEntity<String>> editUser(String username, EditUserRequest request) {

	    return keycloakClient.getUser(username)
	            .flatMap(userInfo -> keycloakClient.editUser(userInfo.getId(), request)
	                    .map(status -> ResponseEntity.status(status).body("EDITED")));
	}

}
