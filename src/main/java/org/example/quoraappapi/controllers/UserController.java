package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.MyProfileResponse;
import org.example.quoraappapi.dtos.UpdateUserRequest;
import org.example.quoraappapi.dtos.UserResponse;
import org.example.quoraappapi.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public MyProfileResponse getMyProfile(Authentication authentication) {
        return userService.getMyProfile(authentication.getName());
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable UUID userId) {
        return userService.getUserProfile(userId);
    }

    @PutMapping("/{userId}")
    public MyProfileResponse updateUser(@PathVariable UUID userId,
                                        @RequestBody UpdateUserRequest request,
                                        Authentication authentication) {
        return userService.updateUser(userId, request, authentication.getName());
    }
}