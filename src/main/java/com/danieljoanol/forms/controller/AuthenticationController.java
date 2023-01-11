package com.danieljoanol.forms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.controller.request.AuthenticationRequest;
import com.danieljoanol.forms.controller.request.RegisterRequest;
import com.danieljoanol.forms.controller.response.AuthenticationResponse;
import com.danieljoanol.forms.service.AuthenticationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Url.API_V1_AUTH)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.ok(Message.MESSAGE_CHECK_EMAIL);
        // TODO: implement email
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
