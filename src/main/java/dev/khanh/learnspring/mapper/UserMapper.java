package dev.khanh.learnspring.mapper;

import dev.khanh.learnspring.dto.request.UserCreationRequest;
import dev.khanh.learnspring.dto.request.UserUpdateRequest;
import dev.khanh.learnspring.dto.respone.UserResponse;
import dev.khanh.learnspring.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
