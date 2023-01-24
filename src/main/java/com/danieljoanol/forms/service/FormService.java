package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.exception.NoParentException;

public interface FormService extends GenericService<Form> {
    
    public Form create(Form form, Long clientId) throws NoParentException;
}
