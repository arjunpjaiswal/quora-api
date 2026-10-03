package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.MyProfileResponse;
import org.example.quoraappapi.dtos.RegisterUserRequest;
import org.example.quoraappapi.dtos.UpdateUserRequest;
import org.example.quoraappapi.dtos.UserResponse;
import org.example.quoraappapi.exceptions.DuplicateResourceException;
import org.example.quoraappapi.exceptions.ForbiddenException;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MyProfileResponse registerUser(RegisterUserRequest request) {
        if (request.getUserName() == null || request.getUserName().isBlank()) {
            throw new IllegalArgumentException("User name is required");
        }
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            throw new IllegalArgumentException("A valid email is required");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException("User already exists");
        }

        User user = User.builder()
                .userName(request.getUserName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        return MyProfileResponse.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserProfile(UUID id) {
        return UserResponse.from(findById(id));
    }

    @Transactional(readOnly = true)
    public MyProfileResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return MyProfileResponse.from(user);
    }

    @Transactional
    public MyProfileResponse updateUser(UUID id, UpdateUserRequest request, String currentEmail) {
        User existingUser = findById(id);

        if (!existingUser.getEmail().equals(currentEmail)) {
            throw new ForbiddenException("You can only edit your own profile");
        }

        if (request.getUserName() != null) existingUser.setUserName(request.getUserName());
        if (request.getBio() != null) existingUser.setBio(request.getBio());
        return MyProfileResponse.from(userRepository.save(existingUser));
    }

    private User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}