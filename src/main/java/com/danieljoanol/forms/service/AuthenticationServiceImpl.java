package com.danieljoanol.forms.service;

import java.util.Set;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
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
    private final TokenBlacklistService blacklistService;
    private final PasswordEncoder encoder;
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
    public User register(RegisterRequest request, boolean mainUser) throws Exception {

        if (userService.existsByUsername(request.getUsername())) {
            throw new DuplicateKeyException(Message.DUPLICATE_USERNAME);
        }

        Role groupRole;
        if (mainUser) {
            if (request.getMaxGroup() == null) request.setMaxGroup(1);
            groupRole = roleService.createGroupRole(request.getMaxGroup());
        } else {
            groupRole = roleService.findByName(JwtTokenUtil.getGroupRole(GROUP_PREFIX));
            if (groupRole.getMaxUsers() == groupRole.getTotalUsers()) {
                throw new AccessDeniedException(Message.MAX_USERS_ERROR);
            } else {
                groupRole.setTotalUsers(groupRole.getTotalUsers() + 1);
            }
        }

        Role userRole = roleService.findByName("ROLE_USER");

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .roles(Set.of(userRole, groupRole))
                .isEnabled(true)
                .build();

        return userService.create(user);
        //FIXME: duplicate key value violates unique constraint (until we pass the ids created in import.sql)
    }

    @Override
    public String logout(String token) {
        SecurityContextHolder.getContext().setAuthentication(null);
        blacklistService.save(token.substring(7));
        return Message.LOG_OUT;
    }

}
