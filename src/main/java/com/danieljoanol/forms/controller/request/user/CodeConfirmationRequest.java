package com.danieljoanol.forms.controller.request.user;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CodeConfirmationRequest {
    
    private Long id;

    @NotNull(message = "#{Message.notNull('code')}")
    private int code;

}
