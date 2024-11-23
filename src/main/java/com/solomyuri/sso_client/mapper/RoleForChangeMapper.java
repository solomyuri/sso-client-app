package com.solomyuri.sso_client.mapper;

import com.solomyuri.sso_client.enums.GameRole;
import com.solomyuri.sso_client.model.keycloak.RoleForChange;

public interface RoleForChangeMapper {

	RoleForChange toRoleForChange(GameRole role);
}
