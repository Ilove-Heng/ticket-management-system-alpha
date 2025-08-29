package com.nangseakheng.user.service.impl;

import com.nangseakheng.common.constant.ApiConstant;
import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.common.exception.SystemException;
import com.nangseakheng.user.constant.Constant;
import com.nangseakheng.user.dto.request.CreatePermissionRequest;
import com.nangseakheng.user.dto.response.PermissionResponse;
import com.nangseakheng.user.entity.Permission;
import com.nangseakheng.user.entity.Role;
import com.nangseakheng.user.repository.PermissionRepository;
import com.nangseakheng.user.repository.RoleRepository;
import com.nangseakheng.user.service.PermissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public ResponseErrorTemplate create(CreatePermissionRequest permissionRequest) {
        if (permissionRepository.existsByName(permissionRequest.name())) {
            throw new SystemException("Permission with name " + permissionRequest.name() + " already exists");
        }
        Permission permission = mapToPermission(permissionRequest);
        permissionRepository.save(permission);
        return new ResponseErrorTemplate(
                ApiConstant.SUCCESS.getDescription(),
                ApiConstant.SUCCESS.getKey(),
                constructPermissionResponse(permission),
                false);
    }

    @Override
    public ResponseErrorTemplate update(Long id, CreatePermissionRequest permissionDetails) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new SystemException("Permission with id " + id + " not found"));
        if (!permission.getName().equals(permissionDetails.name()) &&
                permissionRepository.existsByName(permissionDetails.name())) {
            throw new SystemException("Permission with name " + permissionDetails.name() + " already exists");
        }
        permission.setName(permissionDetails.name());
        permission.setDescription(permissionDetails.description());
        permission.setStatus(permissionDetails.status());
        permissionRepository.save(permission);

        return constructPermissionResponse(permission);
    }

    @Override
    public ResponseErrorTemplate assignRoleToPermission(Long permissionId, Long roleId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new SystemException("Permission with id " + permissionId + " not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new SystemException("Role with id " + roleId + " not found"));

        permission.addRole(role);
        permissionRepository.save(permission);

        return constructPermissionResponse(permission);
    }

    @Override
    public ResponseErrorTemplate removeRoleFromPermission(Long permissionId, Long roleId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new SystemException("Permission with id " + permissionId + " not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new SystemException("Role with id " + roleId + " not found"));

        permission.removeRole(role);
        permissionRepository.save(permission);

        return constructPermissionResponse(permission);
    }

    @Override
    public List<Permission> getPermissionsByNameIn(Set<String> names) {
        return permissionRepository.findAllByStatusAndNameIn(Constant.ACTIVE, names);
    }

    private Permission mapToPermission(CreatePermissionRequest request) {
        return Permission.builder()
                .name(request.name())
                .description(request.description())
                .status(request.status() != null ? request.status() : Constant.ACTIVE)
                .build();
    }

    private ResponseErrorTemplate constructPermissionResponse(Permission permission) {
        PermissionResponse permissionResponse = new PermissionResponse(
                permission.getId(),
                permission.getName(),
                permission.getDescription(),
                permission.getStatus(),
                permission.getRoles(),
                permission.getGroups()
        );
        return new ResponseErrorTemplate(
                ApiConstant.SUCCESS.getDescription(),
                ApiConstant.SUCCESS.getKey(),
                permissionResponse,
                false);
    }

}
