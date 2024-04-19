package com.easybook.authservice.dto;

import lombok.Data;

@Data
public class UserCredentialRegisterRequest {
  private String login;
  private String email;
  private String password;
}
