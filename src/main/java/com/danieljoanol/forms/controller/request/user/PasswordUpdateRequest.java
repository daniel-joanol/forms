package com.danieljoanol.forms.controller.request.user;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PasswordUpdateRequest {
    
    @NotBlank(message = "#{Message.notBlank('email')}")
    private String email;

    @NotBlank(message = "#{Message.notBlank('newPassword')}")
    private String newPassword;

}
