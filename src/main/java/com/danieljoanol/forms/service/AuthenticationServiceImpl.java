package com.danieljoanol.forms.service;

import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.controller.request.AuthenticationRequest;
import com.danieljoanol.forms.controller.request.RegisterRequest;
import com.danieljoanol.forms.controller.response.AuthenticationResponse;
import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtils;
    private final AuthenticationManager authManager;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) throws AuthenticationException {

        User user = userService.findByUsername(request.getUsername());
        if (!user.isEnabled()) {
            throw new AuthenticationException(Message.userBlocked(request.getUsername()));
        }

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtils.generateJwtToken(authentication);
        
        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .token(jwt)
                .build();
    }

    @Override
    public void register(RegisterRequest request) {

        if (userService.existsByUsername(request.getUsername())) {
            throw new DuplicateKeyException(Message.DUPLICATE_USERNAME);
        }

        Role userRole = roleService.findByName("USER");

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .isEnabled(false)
                .build();

        userService.create(user);
    }

    @Override
    public ResponseCookie logout() {
        //TODO: logout
        return null;
    }

}
