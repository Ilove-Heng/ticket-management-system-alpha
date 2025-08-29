package com.nangseakheng.user.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nangseakheng.user.entity.Group;
import com.nangseakheng.user.entity.Role;

import java.util.Set;

public record CreatePermissionRequest(
        String name,
        String description,
        String status,
        @JsonProperty("roles") Set<Role> roles,
        @JsonProperty("groups") Set<Group> groups) {

}
