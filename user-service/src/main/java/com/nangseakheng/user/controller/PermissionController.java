package com.nangseakheng.user.controller;

import com.nangseakheng.common.dto.ApiResponse;
import com.nangseakheng.user.dto.request.CreateGroupRequestDTO;
import com.nangseakheng.user.dto.request.GroupMemberRequestDTO;
import com.nangseakheng.user.dto.response.GroupResponseDTO;
import com.nangseakheng.user.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    private ResponseEntity<ApiResponse<GroupResponseDTO>> createGroup(@Valid @RequestBody CreateGroupRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.<GroupResponseDTO>success("Create group successfully", groupService.create(request)));
    }

    @PutMapping("/{groupId}")
    private ResponseEntity<ApiResponse<GroupResponseDTO>> updateGroup(@PathVariable Long groupId, @Valid @RequestBody CreateGroupRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.<GroupResponseDTO>success("Update group successfully", groupService.update(groupId, request)));
    }

    @PostMapping("/{groupId}/members")
    private ResponseEntity<ApiResponse<GroupResponseDTO>> addMemberToGroup(@PathVariable Long groupId, @Valid @RequestBody GroupMemberRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.<GroupResponseDTO>success("Add member to group successfully", groupService.addMemberToGroup(groupId, request)));
    }

    @DeleteMapping("/{groupId}/members")
    private ResponseEntity<ApiResponse<GroupResponseDTO>> removeMemberFromGroup(@PathVariable Long groupId, @Valid @RequestBody GroupMemberRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.<GroupResponseDTO>success("Remove member from group successfully", groupService.removeMemberFromGroup(groupId, request)));
    }
}
