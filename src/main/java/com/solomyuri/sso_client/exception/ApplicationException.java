package com.solomyuri.sso_client.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ApplicationException extends RuntimeException {

	private final HttpStatus status;

	public ApplicationException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

}
