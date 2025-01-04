package com.solomyuri.sso_client.model.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakError {

	@JsonProperty("error")
	private String error;
	@JsonProperty("error_description")
	private String errorDescription;
	@JsonProperty("errorMessage")
	private String errorMessage;
}
