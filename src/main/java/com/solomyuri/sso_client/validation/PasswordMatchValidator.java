package com.solomyuri.sso_client.validation;

import java.util.Objects;

import com.solomyuri.sso_client.model.RegistrationRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatcher, RegistrationRequest> {

	@Override
	public boolean isValid(RegistrationRequest userDto, ConstraintValidatorContext context) {

		return Objects.equals(userDto.getPassword(), userDto.getPasswordConfirm());
	}

}