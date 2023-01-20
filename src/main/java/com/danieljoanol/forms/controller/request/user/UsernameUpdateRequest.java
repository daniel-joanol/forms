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
    
    private Long id;

    @NotBlank(message = "#{Message.notBlank('newUsername')}")
    private String newUsername;
    
}
