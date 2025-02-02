package dev.khanh.learnspring.mapper;

import dev.khanh.learnspring.dto.request.RoleRequest;
import dev.khanh.learnspring.dto.respone.RoleResponse;
import dev.khanh.learnspring.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}
