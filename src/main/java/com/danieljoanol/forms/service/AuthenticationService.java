package com.danieljoanol.forms.service;

import javax.naming.AuthenticationException;

import com.danieljoanol.forms.controller.request.AuthenticationRequest;
import com.danieljoanol.forms.controller.response.AuthenticationResponse;

public interface AuthenticationService {

    public AuthenticationResponse login(AuthenticationRequest request) throws AuthenticationException;

    public String logout(String token);
    
}
