package com.danieljoanol.forms.controller.request;

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
    
    @NotBlank(message = "#{Message.notBlank('firstName')}")
    private String firstName;

    @NotBlank(message = "#{Message.notBlank('lastName')}")
    private String lastName;

    @NotBlank(message = "#{Message.notBlank('username')}")
    private String username;

    @NotBlank(message = "#{Message.notBlank('password')}")
    private String password;

    // To be used when anyone can register, not just the admin

}
