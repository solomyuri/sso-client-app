package com.solomyuri.sso_client.model.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("expires_in")
	private String expireIn;

	@JsonProperty("refresh_expires_in")
	private String refreshExpireIn;

	@JsonProperty("scope")
	private String scope;

}
