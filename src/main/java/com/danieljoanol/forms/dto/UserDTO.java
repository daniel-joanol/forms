package com.danieljoanol.forms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import com.danieljoanol.forms.entity.Role;
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
public class UserDTO extends GenericDTO<User> {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isEnabled;
    private LocalDate lastPayment;
    private Set<RoleDTO> roles;
    private Date disabledDate;
    private String comments;
    private Integer passwordCode;
    private Integer usernameCode;
    private LocalDateTime passwordTimeLimit;
    private LocalDateTime usernameTimeLimit;

    public UserDTO(User entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.firstName = entity.getFirstName();
            this.lastName = entity.getLastName();
            this.username = entity.getUsername();
            this.isEnabled = entity.isEnabled();
            this.lastPayment = entity.getLastPayment();
            this.roles = entity.getRoles().stream().map(r -> new RoleDTO(r)).collect(Collectors.toSet());
            this.disabledDate = entity.getDisabledDate();
            this.comments = entity.getComments();
            this.passwordCode = entity.getPasswordCode();
            this.usernameCode = entity.getUsernameCode();
            this.passwordTimeLimit = entity.getPasswordTimeLimit();
            this.usernameTimeLimit = entity.getUsernameTimeLimit();
        }
    }

    @Override
    public User toEntity() {

        Set<Role> roleEntities = null;
        if (roles != null) {
            roleEntities = this.roles.stream().map(RoleDTO::toEntity).collect(Collectors.toSet());
        }

        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .isEnabled(isEnabled)
                .lastPayment(lastPayment)
                .disabledDate(disabledDate)
                .comments(comments)
                .passwordCode(passwordCode)
                .usernameCode(usernameCode)
                .passwordTimeLimit(passwordTimeLimit)
                .usernameTimeLimit(usernameTimeLimit)
                .roles(roleEntities)
                .build();
    }

}
