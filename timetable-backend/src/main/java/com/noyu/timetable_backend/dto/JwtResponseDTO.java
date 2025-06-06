package com.noyu.timetable_backend.dto;

public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";

    public JwtResponseDTO(String accessToken) {
        this.token = accessToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
