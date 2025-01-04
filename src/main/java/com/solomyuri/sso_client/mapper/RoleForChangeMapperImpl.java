package com.solomyuri.sso_client.mapper;

import org.springframework.stereotype.Component;

import com.solomyuri.sso_client.enums.GameRole;
import com.solomyuri.sso_client.model.keycloak.RoleForChange;

@Component
public class RoleForChangeMapperImpl implements RoleForChangeMapper {

	@Override
	public RoleForChange toRoleForChange(GameRole role) {
		return new RoleForChange(role.getId(), role.getName());
	}

}
