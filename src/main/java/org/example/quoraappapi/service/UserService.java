package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.RegisterUserRequest;
import org.example.quoraappapi.dtos.UpdateUserRequest;
import org.example.quoraappapi.exceptions.DuplicateResourceException;
import org.example.quoraappapi.exceptions.ForbiddenException;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import org.example.quoraappapi.exceptions.DuplicateResourceException;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterUserRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) throw new DuplicateResourceException("User already exists");
        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))   // new
                .build();

        return userRepository.save(user);
    }
 public User getUserById(UUID id){
     Optional<User> userWithId=userRepository.findById(id);
     if(userWithId.isEmpty()) throw new ResourceNotFoundException("User not found");
     return userWithId.get();
 }

     public User updateUser(UUID id, UpdateUserRequest request, String currentEmail) {
         User existingUser = getUserById(id);

         if (!existingUser.getEmail().equals(currentEmail)) {
             throw new ForbiddenException("You can only edit your own profile");
         }

         if (request.getUserName() != null) existingUser.setUserName(request.getUserName());
         if (request.getBio() != null) existingUser.setBio(request.getBio());
         return userRepository.save(existingUser);
     }


}
