package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.RegisterUserRequest;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/{userId}")
    public User getUser(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

@PostMapping
public ResponseEntity<User> registerUser(@RequestBody RegisterUserRequest request) {
    User created = userService.registerUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
    @PutMapping("/{userId}")
public User updateUser(@PathVariable UUID userId, @RequestBody User user) {
    return userService.updateUser(userId, user);
}
}
