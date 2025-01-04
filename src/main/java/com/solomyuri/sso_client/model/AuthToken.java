package com.solomyuri.sso_client.model;

import java.util.List;

import lombok.Builder;

@Builder
public record AuthToken(String username, List<String> roles, boolean emailVerified, String email) {

}
