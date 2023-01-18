package com.danieljoanol.forms.service;

import org.springframework.http.ResponseCookie;

import com.danieljoanol.forms.controller.request.AuthenticationRequest;
import com.danieljoanol.forms.controller.request.RegisterRequest;
import com.danieljoanol.forms.controller.response.AuthenticationResponse;

public interface AuthenticationService {
    
    public void register(RegisterRequest request);
    public AuthenticationResponse login(AuthenticationRequest request);
    public ResponseCookie logout();
}
