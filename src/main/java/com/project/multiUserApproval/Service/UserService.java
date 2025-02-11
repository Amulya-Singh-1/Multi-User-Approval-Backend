package com.project.multiUserApproval.Service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.multiUserApproval.Model.User;
import com.project.multiUserApproval.Repository.UserRepository;
import com.project.multiUserApproval.Request.UserRequest;
import com.project.multiUserApproval.Response.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserResponse registerUser(UserRequest request) {
    Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
    if (existingUser.isPresent()) {
      throw new RuntimeException("Email already in use.");
    }

    User user = new User(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
    userRepository.save(user);
    return mapToResponse(user);
  }

  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return mapToResponse(user);
  }

  public List<UserResponse> getAllUsers() {
    return userRepository.findAll()
        .stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  private UserResponse mapToResponse(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail());
  }
}
