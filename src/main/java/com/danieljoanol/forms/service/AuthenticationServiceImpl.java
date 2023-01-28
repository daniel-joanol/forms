package com.danieljoanol.forms.service;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.controller.request.AuthenticationRequest;
import com.danieljoanol.forms.controller.response.AuthenticationResponse;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final TokenBlacklistService blacklistService;
    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${forms.app.group}")
    public String GROUP_PREFIX;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) throws AuthenticationException {

        User user = userService.findByUsername(request.getUsername());
        if (!user.isEnabled()) {
            throw new AuthenticationException(Message.userBlocked(request.getUsername()));
        }

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        return new AuthenticationResponse(user, jwt);
    }

    @Override
    public String logout(String token) {
        SecurityContextHolder.getContext().setAuthentication(null);
        blacklistService.save(token.substring(7));
        return Message.LOG_OUT;
    }

}
