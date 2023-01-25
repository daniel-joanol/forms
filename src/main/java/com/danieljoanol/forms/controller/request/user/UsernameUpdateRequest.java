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
public class UsernameUpdateRequest {
    
    @NotBlank(message = "actualUsername {err.blank}")
    private String actualUsername;

    @NotBlank(message = "newUsername {err.blank}")
    private String newUsername;
    
}
