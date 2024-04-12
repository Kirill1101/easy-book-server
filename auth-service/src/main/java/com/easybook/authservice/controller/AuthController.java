package com.easybook.authservice.controller;

import com.easybook.authservice.dto.UserCredentialAuthRequest;
import com.easybook.authservice.dto.UserCredentialRegisterRequest;
import com.easybook.authservice.entity.UserCredential;
import com.easybook.authservice.mapper.UserCredentialMapper;
import com.easybook.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final UserCredentialMapper userCredentialMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserCredentialRegisterRequest credential) {
        UserCredential userCredential = userCredentialMapper.userCredentialRegisterRequestToUserCredential(credential);
        authService.saveUser(userCredential);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody UserCredentialAuthRequest userCredentialAuthRequest) {
        Authentication auth =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userCredentialAuthRequest.getName(), userCredentialAuthRequest.getPassword()));
        if (auth.isAuthenticated()) {
            return authService.generateToken(userCredentialAuthRequest.getName());
        } else {
            throw new RuntimeException("Invalid access");
        }
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestParam("token") String token) {
        try {
            authService.validateToken(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
