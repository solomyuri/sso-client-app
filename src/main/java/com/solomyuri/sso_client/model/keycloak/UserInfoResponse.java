package com.solomyuri.sso_client.model.keycloak;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResponse {

	private UUID id;
	private String username;
	private boolean emailVerified;
	private boolean enabled;
}
