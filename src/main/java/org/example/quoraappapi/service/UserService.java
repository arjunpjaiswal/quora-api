package org.example.quoraappapi.service;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.RegisterUserRequest;
import org.example.quoraappapi.exceptions.ResourceNotFoundException;
import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public User registerUser(RegisterUserRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if(existingUser.isPresent()) throw new RuntimeException("User already exists");

        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .build();

        return userRepository.save(user);
    }
 public User getUserById(UUID id){
     Optional<User> userWithId=userRepository.findById(id);
     if(userWithId.isEmpty()) throw new ResourceNotFoundException("User not found");
     return userWithId.get();
 }
 public User updateUser(UUID id,User updatedUser){
     User existingUser=getUserById(id);


     if (updatedUser.getUserName() != null) existingUser.setUserName(updatedUser.getUserName());
         if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
         if (updatedUser.getBio() != null) existingUser.setBio(updatedUser.getBio());
         return userRepository.save(existingUser);


 }
}
