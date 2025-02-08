package com.solomyuri.sso_client.enums;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameRole {

    USER(UUID.fromString("2dae6c04-9a65-43b9-a35c-334a34516f5c"), "game-service-user"),
    ADMIN(UUID.fromString("97466c54-cc4b-4931-914d-c458a5b58b5c"), "game-service-admin");

    private final UUID id;
    private final String name;
}
