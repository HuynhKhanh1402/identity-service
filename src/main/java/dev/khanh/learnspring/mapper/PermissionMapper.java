package dev.khanh.learnspring.mapper;

import dev.khanh.learnspring.dto.request.PermissionRequest;
import dev.khanh.learnspring.dto.respone.PermissionResponse;
import dev.khanh.learnspring.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);

    PermissionResponse toPermissionResponse(Permission permission);
}
