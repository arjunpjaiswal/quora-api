package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.AuthResponse;
import org.example.quoraappapi.dtos.LoginRequest;
import org.example.quoraappapi.dtos.RegisterUserRequest;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.service.AuthService;
import org.example.quoraappapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(request));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}