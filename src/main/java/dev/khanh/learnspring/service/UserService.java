package dev.khanh.learnspring.service;

import dev.khanh.learnspring.dto.request.UserCreationRequest;
import dev.khanh.learnspring.dto.request.UserUpdateRequest;
import dev.khanh.learnspring.dto.respone.UserResponse;
import dev.khanh.learnspring.entity.User;
import dev.khanh.learnspring.enums.Role;
import dev.khanh.learnspring.exception.AppException;
import dev.khanh.learnspring.exception.ErrorCode;
import dev.khanh.learnspring.mapper.UserMapper;
import dev.khanh.learnspring.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;


    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//        user.setRoles(roles);

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        log.info("In method getUsers");
        return userRepository.findAll();
    }

    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public UserResponse getUser(String userId) {
        log.info("In method getUser {}", userId);
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
}
