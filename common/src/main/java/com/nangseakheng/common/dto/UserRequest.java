package com.nangseakheng.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;

public record UserRequest(
        @JsonProperty("id") Long id,
        @JsonProperty("username") String username,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("user_img") String userImg,
        @JsonProperty("password") String password,
        @JsonProperty("email") String email,
        @JsonProperty("user_type") String userType,
        @JsonProperty("gender") String gender,
        @JsonProperty("date_of_birth") LocalDateTime dateOfBirth,
        @JsonProperty("last_login") LocalDateTime lastLogin,
        @JsonProperty("login_attempt") Integer loginAttempt,
        @JsonProperty("max_attempt") Integer maxAttempt,
        @JsonProperty("enable_allocate") boolean enableAllocate,
        @JsonProperty("status") String status,
        @JsonProperty("roles") Set<String> roles,
        @JsonProperty("groups") Set<String> groups
) {
}
