package com.easybook.authservice.repository;

import com.easybook.authservice.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialsRepository extends JpaRepository<UserCredential, UUID> {
  Optional<UserCredential> findByLogin(String login);
}
