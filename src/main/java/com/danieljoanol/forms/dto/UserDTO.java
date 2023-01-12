package com.danieljoanol.forms.dto;

import java.time.LocalDate;
import java.util.Set;

import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.entity.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDTO extends GenericDTO<User> {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isEnabled;
    private LocalDate nextPayment;
    private ShopDTO shop;
    private Set<Role> roles;

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        this.isEnabled = entity.isEnabled();
        this.nextPayment = entity.getNextPayment();
        this.shop = new ShopDTO(entity.getShop());
        this.roles = entity.getRoles();
    }

    @Override
    public User toEntity() {
        User entity = new User();
        entity.setId(this.id);
        entity.setFirstName(this.firstName);
        entity.setLastName(this.lastName);
        entity.setEmail(this.email);
        entity.setPassword(this.password);
        entity.setEnabled(this.isEnabled);
        entity.setNextPayment(this.nextPayment);
        entity.setShop(this.shop.toEntity());
        entity.setRoles(this.roles);
        return entity;
    }

}
