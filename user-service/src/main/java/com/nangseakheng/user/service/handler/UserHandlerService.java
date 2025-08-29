package com.nangseakheng.user.service.handler;

import com.nangseakheng.common.dto.EmptyObject;
import com.nangseakheng.common.dto.UserRequest;
import com.nangseakheng.common.dto.UserResponse;
import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.user.entity.Role;
import com.nangseakheng.user.entity.User;
import com.nangseakheng.user.repository.RoleRepository;
import com.nangseakheng.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserHandlerService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserHandlerService(PasswordEncoder passwordEncoder,
                              UserRepository userRepository,
                              RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public ResponseErrorTemplate userRequestValidation(UserRequest userRequest) {

        if(ObjectUtils.isEmpty(userRequest.password())) {
            return new ResponseErrorTemplate(
                    "Password can't be blank or null.",
                    String.valueOf(HttpStatus.BAD_REQUEST),
                    new EmptyObject(),
                    true);
        }

        Optional<User> user = userRepository.findByUsernameOrEmail(userRequest.username(), userRequest.email());
        if(user.isPresent()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseErrorTemplate(
                            "Username or Email is already taken.",
                            String.valueOf(HttpStatus.BAD_REQUEST),
                            new EmptyObject(),
                            true)).getBody();
        }

        List<String> roles = roleRepository.findAll().stream().map(Role::getName).toList();
        for(var role : userRequest.roles()){
            if(!roles.contains(role)) {
                return new ResponseErrorTemplate(
                        "Role is invalid request.",
                        String.valueOf(HttpStatus.BAD_REQUEST),
                        new EmptyObject(),
                        true);
            }
        }
        return new ResponseErrorTemplate(
                "Success",
                String.valueOf(HttpStatus.OK),
                new EmptyObject(),
                false);
    }

    public User mapUserRequestToUser(final UserRequest userRequest, User user) {
        user.setUsername(userRequest.username());
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setUserImage(userRequest.userImg());
        user.setEmail(userRequest.email());
        user.setGender(userRequest.gender());
        user.setDateOfBirth(userRequest.dateOfBirth());
        user.setUserType(userRequest.userType());
        user.setEnableAllocate(userRequest.enableAllocate());
        user.setMaxAttempt(userRequest.maxAttempt());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setLoginAttempt(Optional.ofNullable(userRequest.loginAttempt()).orElse(0));

        return user;
    }

    public UserResponse mapUserToUserResponse(final User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserImage(),
                user.getGender(),
                user.getDateOfBirth(),
                user.getPassword(),
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                user.getCreatedAt(),
                user.getLastLogin(),
                user.getLoginAttempt(),
                user.getMaxAttempt(),
                user.getStatus(),
                user.getRoles().stream().map(Role::getName).toList()
        );
    }
}