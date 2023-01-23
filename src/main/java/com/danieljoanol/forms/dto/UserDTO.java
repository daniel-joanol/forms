package com.danieljoanol.forms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private boolean isEnabled;
    private LocalDate lastPayment;
    private ShopDTO shop;
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

            if (entity.getShop() != null) {
                this.shop = new ShopDTO(entity.getShop());
            }
        }
    }

    public static UserDTO getPublicUserDTO(User entity) {
        UserDTO publicDto = new UserDTO();

        if (entity != null) {
            publicDto.setId(entity.getId());
            publicDto.setUsername(entity.getUsername());
            publicDto.setFirstName(entity.getFirstName());
            publicDto.setLastName(entity.getLastName());
            publicDto.setLastPayment(entity.getLastPayment());
            if (entity.getShop() != null) {
                publicDto.setShop(new ShopDTO(entity.getShop()));
            }
        }

        return publicDto;
    }

    @Override
    public User toEntity() {
        User entity = new User();
        entity.setId(this.id);
        entity.setFirstName(this.firstName);
        entity.setLastName(this.lastName);
        entity.setUsername(this.username);
        entity.setEnabled(this.isEnabled);
        entity.setLastPayment(this.lastPayment);
        entity.setShop(this.shop.toEntity());
        entity.setRoles(this.roles.stream().map(RoleDTO::toEntity).collect(Collectors.toSet()));
        entity.setDisabledDate(this.disabledDate);
        entity.setComments(this.comments);
        entity.setPasswordCode(this.passwordCode);
        entity.setUsernameCode(this.usernameCode);
        entity.setPasswordTimeLimit(this.passwordTimeLimit);
        entity.setUsernameTimeLimit(this.usernameTimeLimit);
        return entity;
    }

}
