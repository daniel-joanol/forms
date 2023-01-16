package com.danieljoanol.forms.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    
    @NotBlank(message = "email is mandatory")
    private String email;

    @NotEmpty(message = "password is mandatory")
    private String password;
    
}
