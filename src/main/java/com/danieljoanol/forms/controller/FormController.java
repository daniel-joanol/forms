package com.danieljoanol.forms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danieljoanol.forms.assembler.FormAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.FormDTO;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.repository.FormRepository;
import com.danieljoanol.forms.service.FormService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(Url.FORM)
@Api(value = "Form Controller", description = "Controller to manage forms")
public class FormController extends GenericController<Form, FormDTO> {
    
    @Autowired
    private FormService formService;

    public FormController(FormRepository formRepository, FormAssembler formAssembler) {
        super(formRepository, formAssembler);
    }

}
