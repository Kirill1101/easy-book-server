package com.easybook.authservice.util;

import com.easybook.authservice.entity.Role;
import com.easybook.authservice.entity.UserCredential;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
  private final String name;
  private final String password;
  private final List<Role> roles;

  public CustomUserDetails(UserCredential userCredential) {
    this.name = userCredential.getName();
    this.password = userCredential.getPassword();
    this.roles = userCredential.getRoles();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String[] userRoles = roles.stream().map(Enum::name).toArray(String[]::new);
    return AuthorityUtils.createAuthorityList(userRoles);
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
