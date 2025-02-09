package com.solomyuri.sso_client.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solomyuri.sso_client.model.ChangeRoleRequest;
import com.solomyuri.sso_client.model.EditUserRequest;
import com.solomyuri.sso_client.model.RegistrationRequest;
import com.solomyuri.sso_client.model.keycloak.UserInfoResponse;
import com.solomyuri.sso_client.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<String>> createUser(@Validated @RequestBody RegistrationRequest request) {

	return userService.createUser(request);
    }
    
    @GetMapping()
    public Mono<ResponseEntity<List<UserInfoResponse>>> createUser(@RequestParam String username) {
	
	return userService.getUserInfo(username);
    }

    @DeleteMapping()
    public Mono<ResponseEntity<String>> deleteUser(@RequestParam String username) {

	return userService.deleteUser(username);
    }

    @PostMapping(path = "/roles", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<String>> changeRoles(@Validated @RequestBody ChangeRoleRequest request) {

	return userService.changeRole(request);
    }

    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<String>> editUser(@RequestParam String username, 
                                                 @Validated @RequestBody EditUserRequest request) {

	return userService.editUser(username, request);
    }
}
