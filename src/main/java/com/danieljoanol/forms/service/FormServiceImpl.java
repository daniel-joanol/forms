package com.danieljoanol.forms.service;

import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.repository.FormRepository;

@Service
public class FormServiceImpl extends GenericServiceImpl<Form> implements FormService {
    
    private final FormRepository formRepository;

    public FormServiceImpl(FormRepository formRepository) {
        super(formRepository);
        this.formRepository = formRepository;
    }

}
