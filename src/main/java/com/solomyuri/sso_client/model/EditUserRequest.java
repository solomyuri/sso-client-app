package com.solomyuri.sso_client.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditUserRequest {
    
    @Email
    private String email;
    private Boolean emailVerified;
    private Boolean enabled;
}
