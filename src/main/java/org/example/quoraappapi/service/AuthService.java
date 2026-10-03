package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.AuthResponse;
import org.example.quoraappapi.dtos.LoginRequest;
import org.example.quoraappapi.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
        String email = request.getEmail().trim().toLowerCase();

        // loads the user via CustomUserDetailsService and compares the
        // raw password against the stored BCrypt hash; throws if wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword()));

        return new AuthResponse(jwtService.generateToken(email));
    }
}