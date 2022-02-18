package com.innovator.innovator.payload.request;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {

    private String username;
    private String password;
    private Set<String> role;
}
