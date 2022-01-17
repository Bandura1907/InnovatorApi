package com.innovator.innovator.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Integer id;
    private String username;
    private String roles;

    public JwtResponse(String accessToken, Integer id, String username, String roles) {
        token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
