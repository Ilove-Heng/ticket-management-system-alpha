package com.nangseakheng.user.service;

import com.nangseakheng.common.dto.UserRequest;
import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.user.dto.request.UserChangePasswordRequest;
import com.nangseakheng.user.dto.request.UserFilterRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface UserService {

    ResponseErrorTemplate create(UserRequest userRequest);

    ResponseErrorTemplate update(Long id, UserRequest userRequest);

    ResponseErrorTemplate findById(Long id);

    @Transactional(readOnly = true)
    ResponseErrorTemplate findAll(UserFilterRequest filterRequest);

    ResponseErrorTemplate findByUsername(String username);

    ResponseErrorTemplate changePassword(Long id, UserChangePasswordRequest userChangePasswordRequest);

    ResponseErrorTemplate disActivateUser(Set<Long> ids, String status);

    ResponseErrorTemplate resetPassword(Set<Long> ids);

}
