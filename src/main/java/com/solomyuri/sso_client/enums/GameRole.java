package com.solomyuri.sso_client.enums;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameRole {

	ADMIN(UUID.fromString("fe946e08-f5af-4f5e-bccc-56ed21a0ddd9"), "game-service-admin");

	private final UUID id;
	private final String name;
}
