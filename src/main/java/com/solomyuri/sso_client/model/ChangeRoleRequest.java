package com.solomyuri.sso_client.model;

import java.util.List;

import com.solomyuri.sso_client.enums.GameRole;
import com.solomyuri.sso_client.enums.RoleAction;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleRequest {

	@NotEmpty(message = "username must be not empty")
	@Pattern(regexp = "^[a-zA-Z0-9\\.\\-\\_\\@]+$", message = "the username field must contain Latin characters, numbers or special characters - _ @ .")
	@Size(min = 4, max = 32, message = "username length must be between 4 and 16 chars")
	private String username;

	@NotNull(message = "action must be not empty")
	private RoleAction action;

	@NotEmpty(message = "roles must be not null")
	private List<GameRole> roles;

}
