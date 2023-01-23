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
public class AuthenticationRequest {
    
    @NotBlank(message = "#{Message.notBlank('email')}")
    private String username;

    @NotBlank(message = "#{Message.notBlank('password')}")
    private String password;
    
}
