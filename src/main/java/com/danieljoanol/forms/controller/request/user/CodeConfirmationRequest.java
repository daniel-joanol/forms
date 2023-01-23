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
    
    @NotNull(message = "id {err.null}")
    private Long id;

    @NotNull(message = "code {err.null}")
    private int code;

}
