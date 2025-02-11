package com.project.multiUserApproval.Request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserRequest {
  @NotBlank
  @Size(min = 3, max = 50)
  private String name;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 6, max = 20)
  private String password;
}

