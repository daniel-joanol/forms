package com.danieljoanol.forms.controller.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "firstName {err.blank}")
    private String firstName;

    @NotBlank(message = "firstName {err.blank}")
    private String lastName;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            message = "{err.email}")
    private String username;

    @NotBlank(message = "#{Message.notBlank('password')}")
    private String password;

    // To be used when anyone can register, not just the admin

}
