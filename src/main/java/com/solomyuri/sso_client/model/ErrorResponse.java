package com.solomyuri.sso_client.model;

import lombok.Builder;

@Builder
public record ErrorResponse(int errorCode, String errorDescription) {

}
