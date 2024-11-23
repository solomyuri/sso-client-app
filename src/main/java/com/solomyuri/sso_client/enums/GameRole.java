package com.solomyuri.sso_client.enums;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameRole {

	ADMIN(UUID.fromString("9bd3e634-8611-4cf5-bee3-1f8341fc596c"), "game-service-admin");

	private UUID id;
	private String name;
}
