package com.nangseakheng.user.service;


import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.user.entity.CustomUserDetail;
import io.jsonwebtoken.Claims;

import java.security.Key;

public interface JwtService {

    Claims extractClaims(String token);
    Key getKey();
    String generateToken(CustomUserDetail customUserDetail);
    String refreshToken(CustomUserDetail customUserDetail);
    boolean isValidToken(String token);
    ResponseErrorTemplate verifyToken(String authorizationHeader);

}
