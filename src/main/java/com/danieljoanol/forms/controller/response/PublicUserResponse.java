package com.danieljoanol.forms.controller.response;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import com.danieljoanol.forms.dto.RoleDTO;
import com.danieljoanol.forms.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicUserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Set<RoleDTO> roles;
    private LocalDate lastPayment;

    public PublicUserResponse(User entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.username = entity.getUsername();
            this.firstName = entity.getFirstName();
            this.lastName = entity.getLastName();
            this.roles = entity.getRoles().stream().map(RoleDTO::new).collect(Collectors.toSet());
            this.lastPayment = entity.getLastPayment();
        }
    }
}
