package com.nangseakheng.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {
    private String token;

    @JsonProperty("token_type")
    private String tokenType;
    private String username;
    private String role;
}
