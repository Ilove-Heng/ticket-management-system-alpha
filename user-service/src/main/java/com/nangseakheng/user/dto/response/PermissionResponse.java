package com.nangseakheng.user.dto.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nangseakheng.user.entity.Group;
import com.nangseakheng.user.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class PermissionResponseDTO {

    private Long id;

    private String name;

    private String description;

    private String status;

    @JsonProperty("roles")
    private Set<Role> roles;

    @JsonProperty("groups")
    private Set<Group> groups;
}
