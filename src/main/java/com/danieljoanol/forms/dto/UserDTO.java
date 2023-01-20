package com.danieljoanol.forms.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

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
    private String password;
    private boolean isEnabled;
    private LocalDate lastPayment;
    private ShopDTO shop;
    private Set<RoleDTO> roles;
    private Date disabledDate;
    private String comments;

    public UserDTO(User entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.firstName = entity.getFirstName();
            this.lastName = entity.getLastName();
            this.username = entity.getUsername();
            this.password = entity.getPassword();
            this.isEnabled = entity.isEnabled();
            this.lastPayment = entity.getLastPayment();
            this.shop = new ShopDTO(entity.getShop());
            this.roles = entity.getRoles().stream().map(r -> new RoleDTO(r)).collect(Collectors.toSet());
            this.disabledDate = entity.getDisabledDate();
            this.comments = entity.getComments();
        }
    }

    @Override
    public User toEntity() {
        User entity = new User();
        entity.setId(this.id);
        entity.setFirstName(this.firstName);
        entity.setLastName(this.lastName);
        entity.setUsername(this.username);
        entity.setPassword(this.password);
        entity.setEnabled(this.isEnabled);
        entity.setLastPayment(this.lastPayment);
        entity.setShop(this.shop.toEntity());
        entity.setRoles(this.roles.stream().map(RoleDTO::toEntity).collect(Collectors.toSet()));
        entity.setDisabledDate(this.disabledDate);
        entity.setComments(this.comments);
        return entity;
    }

}
