package com.easybook.authservice.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "user_credential")
public class UserCredential {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;

  @Column(name = "login", nullable = false, unique = true)
  private String login;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password")
  private String password;
}
