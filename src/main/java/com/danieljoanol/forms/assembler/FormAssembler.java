package com.danieljoanol.forms.assembler;

import org.springframework.stereotype.Component;

import com.danieljoanol.forms.dto.FormDTO;
import com.danieljoanol.forms.entity.Form;

@Component
public class FormAssembler extends GenericAssembler<Form, FormDTO> {
    
    @Override
    public FormDTO convertToDTO(Form entity) {
        return new FormDTO(entity);
    }
}
