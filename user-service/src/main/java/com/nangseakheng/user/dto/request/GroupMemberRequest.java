package com.nangseakheng.user.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class GroupMemberRequestDTO {
    @NotNull(message = "users ids are required")
    private List<Long> userIds;
    public List<Long> getUserIds() {
        return userIds;
    }
}
