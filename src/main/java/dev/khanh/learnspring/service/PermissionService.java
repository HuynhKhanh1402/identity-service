package dev.khanh.learnspring.service;

import dev.khanh.learnspring.dto.request.PermissionRequest;
import dev.khanh.learnspring.dto.respone.PermissionResponse;
import dev.khanh.learnspring.entity.Permission;
import dev.khanh.learnspring.mapper.PermissionMapper;
import dev.khanh.learnspring.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        Permission createdPerm = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(createdPerm);
    }

    public List<PermissionResponse> findAll() {
        return permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
