package com.easybook.authservice.mapper;

import com.easybook.authservice.dto.UserCredentialRegisterRequest;
import com.easybook.authservice.entity.UserCredential;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCredentialMapper {

  UserCredential userCredentialRegisterRequestToUserCredential(
      UserCredentialRegisterRequest userCredentialRegisterRequest);
}
