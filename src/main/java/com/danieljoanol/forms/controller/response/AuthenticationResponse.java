package com.danieljoanol.forms.controller.response;

import java.util.Set;
import java.util.stream.Collectors;

import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Set<String> roles;

    private String token;

    public AuthenticationResponse(User entity, String token) {
        this.username = entity.getUsername();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.roles = entity.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        this.token = token;
    }
}
