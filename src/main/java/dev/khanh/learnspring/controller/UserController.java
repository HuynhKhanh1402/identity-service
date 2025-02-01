package dev.khanh.learnspring.controller;

import dev.khanh.learnspring.dto.request.ApiResponse;
import dev.khanh.learnspring.dto.request.UserCreationRequest;
import dev.khanh.learnspring.dto.request.UserUpdateRequest;
import dev.khanh.learnspring.dto.respone.UserResponse;
import dev.khanh.learnspring.entity.User;
import dev.khanh.learnspring.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping()
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> response = new ApiResponse<>();
        response.setResult(userService.createUser(request));
        return response;
    }

    @GetMapping()
    public List<User> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        log.info("Roles: {}", authentication.getAuthorities());
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/myInfo")
    public UserResponse getMyInfo() {
        return userService.getMyInfo();
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
