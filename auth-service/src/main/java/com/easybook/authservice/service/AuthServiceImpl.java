package com.easybook.authservice.service;

import com.easybook.authservice.entity.UserCredential;
import com.easybook.authservice.repository.UserCredentialsRepository;
import com.easybook.authservice.util.JwtUtil;
//import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final UserCredentialsRepository userCredentialsRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtUtil jwtUtil;
  @Override
  public UserCredential saveUser(UserCredential credential) {
    credential.setPassword(passwordEncoder.encode(credential.getPassword()));
    userCredentialsRepository.save(credential);
    return credential;
  }

  @Override
  public String generateToken(String login) {
    return jwtUtil.generateToken(userCredentialsRepository.findByLogin(login)
        .orElseThrow());
  }

  @Override
  public void validateToken(String token) {
    jwtUtil.validateToken(token);
  }
}
