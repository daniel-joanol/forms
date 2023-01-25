package com.danieljoanol.forms.service;

import javax.naming.AuthenticationException;

import com.danieljoanol.forms.controller.request.AuthenticationRequest;
import com.danieljoanol.forms.controller.request.RegisterRequest;
import com.danieljoanol.forms.controller.response.AuthenticationResponse;
import com.danieljoanol.forms.entity.User;

public interface AuthenticationService {

    public User register(RegisterRequest request, boolean mainUser);

    public AuthenticationResponse login(AuthenticationRequest request) throws AuthenticationException;

    public String logout(String token);
    
}
