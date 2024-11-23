package com.solomyuri.sso_client.model.keycloak;

import java.util.List;

public record CreateUserRequest(String username, boolean enabled, List<Credentials> credentials) {

	public CreateUserRequest(String username, String password) {
		this(username, true, List.of(new Credentials(password)));
	}

	public record Credentials(String type, String value, boolean temporary) {

		public Credentials(String password) {
			this("password", password, false);
		}
	}
}
