package org.example.quoraappapi.service;

import org.example.quoraappapi.models.User;
import org.example.quoraappapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
 public User   registerUser( User user) {
     String email = user.getEmail();
     Optional<User> existingUser = userRepository.findByEmail(email);
     if (existingUser.isEmpty())
     return     userRepository.save(user);

     else {
         throw new RuntimeException("User already exists");
     }
 }
 public Optional<User> getUserById(UUID id){
     Optional<User> userWithId=userRepository.findById(id);
     if(userWithId.isEmpty()) throw new RuntimeException("User not found");
     return userWithId;
 }
 public User updateUser(UUID id,User updatedUser){
     Optional<User>user=getUserById(id);

User existing =user.get();
     if (updatedUser.getUserName() != null) existing.setUserName(updatedUser.getUserName());
         if (updatedUser.getEmail() != null) existing.setEmail(updatedUser.getEmail());
         if (updatedUser.getBio() != null) existing.setBio(updatedUser.getBio());
         return userRepository.save(existing);


 }
}
