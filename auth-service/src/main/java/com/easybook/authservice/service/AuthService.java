package com.easybook.authservice.service;

import com.easybook.authservice.entity.UserCredential;

public interface AuthService {
  UserCredential saveUser(UserCredential credential);

  String generateToken(String login);

  void validateToken(String token);
}
