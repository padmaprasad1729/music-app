package com.agap.service;


import com.agap.model.JwtAuthenticationResponse;
import com.agap.model.SignUpRequest;
import com.agap.model.SigninRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}