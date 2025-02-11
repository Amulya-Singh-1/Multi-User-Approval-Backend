package com.project.multiUserApproval.Controller;

import com.project.multiUserApproval.Service.AuthenticationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthenticationService authenticationService;

  public AuthController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
    String token = authenticationService.authenticate(email, password);
    return ResponseEntity.ok(token);
  }
}
