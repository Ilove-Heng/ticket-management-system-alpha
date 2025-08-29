package com.nangseakheng.user.service;


import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.user.dto.request.AuthenticationRequest;

public interface AuthService {

    ResponseErrorTemplate login(AuthenticationRequest authenticationRequest);
}