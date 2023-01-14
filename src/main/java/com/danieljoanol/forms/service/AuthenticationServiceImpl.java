package com.danieljoanol.forms.service;

import java.util.Set;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.controller.request.AuthenticationRequest;
import com.danieljoanol.forms.controller.request.RegisterRequest;
import com.danieljoanol.forms.controller.response.AuthenticationResponse;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.entity.enums.Role;
import com.danieljoanol.forms.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));

        User user = userService.findByEmail(request.getEmail());
        String token = jwtService.generateToken(user);
        
        return AuthenticationResponse.builder().token(token).build();
    }

    @Override
    public void register(RegisterRequest request) {
        
        if (userService.existsByEmail(request.getEmail())) {
            throw new DuplicateKeyException("Username already in use");
        }
        
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Role.USER))
                .isEnabled(false)
                .build();

        userService.create(user);
    }
    
}
