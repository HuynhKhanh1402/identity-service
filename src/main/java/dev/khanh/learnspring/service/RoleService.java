package dev.khanh.learnspring.service;

import dev.khanh.learnspring.dto.request.RoleRequest;
import dev.khanh.learnspring.dto.respone.RoleResponse;
import dev.khanh.learnspring.entity.Permission;
import dev.khanh.learnspring.entity.Role;
import dev.khanh.learnspring.mapper.RoleMapper;
import dev.khanh.learnspring.repository.PermissionRepository;
import dev.khanh.learnspring.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest roleRequest) {
        Role role = roleMapper.toRole(roleRequest);

        List<Permission> permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        RoleResponse roleResponse = roleMapper.toRoleResponse(roleRepository.save(role));
        roleResponse.setPermissions(roleResponse.getPermissions());

        return roleResponse;
    }

    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String name) {
        roleRepository.deleteById(name);
    }
}
