package com.solomyuri.sso_client.model;

import com.solomyuri.sso_client.validation.ClassValidation;
import com.solomyuri.sso_client.validation.PasswordMatcher;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatcher(groups = ClassValidation.class)
@GroupSequence({ RegistrationRequest.class, ClassValidation.class })
public class RegistrationRequest {

	@NotEmpty(message = "username must be not empty")
	@Pattern(regexp = "^[a-zA-Z0-9\\.\\-\\_\\@]+$", message = "the username field must contain Latin characters, numbers or special characters - _ @ .")
	@Size(min = 4, max = 32, message = "username length must be between 4 and 16 chars")
	private String username;

	@NotEmpty(message = "password must be not empty")
	@Size(min = 8, max = 64, message = "password length must be between 8 and 64 chars")
	private String password;

	@NotEmpty(message = "passwordConfirm must be not empty")
	@Size(min = 8, max = 64, message = "passwordConfirm length must be between 8 and 64 chars")
	private String passwordConfirm;

}
