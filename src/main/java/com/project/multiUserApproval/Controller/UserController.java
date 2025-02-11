package com.project.multiUserApproval.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.multiUserApproval.Request.UserRequest;
import com.project.multiUserApproval.Response.UserResponse;
import com.project.multiUserApproval.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest request) {
    return ResponseEntity.ok(userService.registerUser(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }
}

