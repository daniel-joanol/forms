package com.danieljoanol.forms.controller.request.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UsernameUpdateRequest {
    
    @NotNull(message = "id {err.null}")
    private Long id;

    @NotBlank(message = "newUsername {err.blank}")
    private String newUsername;
    
}
