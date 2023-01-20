package com.danieljoanol.forms.controller.request.user;

import javax.validation.constraints.NotBlank;

import com.danieljoanol.forms.constants.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NamesUpdateRequest {
    
    private Long id;

    @NotBlank(message = "#{Message.notBlank('firstName')}")
    private String firstName;

    @NotBlank(message = "#{Message.notBlank('lastName')}")
    private String lastName;

}
