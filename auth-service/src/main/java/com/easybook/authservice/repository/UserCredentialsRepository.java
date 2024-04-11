package com.easybook.authservice.repository;

import com.easybook.authservice.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialsRepository extends JpaRepository<UserCredential, Long> {
  Optional<UserCredential> findByName(String name);
}
