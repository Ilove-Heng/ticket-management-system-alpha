package com.nangseakheng.user.service;


import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.user.dto.request.CreateGroupRequest;
import com.nangseakheng.user.dto.request.GroupMemberRequest;

public interface GroupService {

    ResponseErrorTemplate createGroup(CreateGroupRequest request);

    ResponseErrorTemplate updateGroup(Long id, CreateGroupRequest request);

    ResponseErrorTemplate addMembersToGroup(Long groupId, GroupMemberRequest groupMemberRequest);

    ResponseErrorTemplate removeMembersFromGroup(Long groupId, GroupMemberRequest groupMemberRequest);
}