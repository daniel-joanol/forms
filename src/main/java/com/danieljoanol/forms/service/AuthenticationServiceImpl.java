package com.danieljoanol.forms.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.controller.request.AuthenticationRequest;
import com.danieljoanol.forms.controller.request.RegisterRequest;
import com.danieljoanol.forms.controller.response.AuthenticationResponse;
import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.entity.enums.ERole;
import com.danieljoanol.forms.security.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authManager;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
        Set<String> roles = user.getAuthorities().stream().map(i -> i.getAuthority()).collect(Collectors.toSet());

        return AuthenticationResponse.builder().email(user.getEmail()).firstName(user.getFirstName())
                .lastName(user.getLastName()).roles(roles).token(jwtCookie.toString()).build();
    }

    @Override
    public void register(RegisterRequest request) {

        if (userService.existsByEmail(request.getEmail())) {
            throw new DuplicateKeyException("Username already in use");
        }

        Role userRole = roleService.findByName(ERole.USER);

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .isEnabled(false)
                .build();

        userService.create(user);
    }

    @Override
    public ResponseCookie logout() {
        return jwtUtils.getCleanJwtCookie();
    }

}
