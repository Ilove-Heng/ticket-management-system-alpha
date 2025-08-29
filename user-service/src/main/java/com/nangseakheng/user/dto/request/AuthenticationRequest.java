package com.nangseakheng.user.dto.request;


public record AuthenticationRequest(
        String username,
        String password){}