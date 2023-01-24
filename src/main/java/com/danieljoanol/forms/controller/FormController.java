package com.danieljoanol.forms.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danieljoanol.forms.assembler.FormAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.FormDTO;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.exception.NoParentException;
import com.danieljoanol.forms.repository.FormRepository;
import com.danieljoanol.forms.service.FormService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(Url.FORM)
public class FormController extends GenericController<Form, FormDTO> {
    
    private final FormService formService;
    private final FormAssembler formAssembler;

    public FormController(FormRepository formRepository, FormAssembler formAssembler, FormService formService) {
        super(formRepository, formAssembler);
        this.formAssembler = formAssembler;
        this.formService = formService;
    }

    @Operation(summary = "Create", description = "Method to create a new form")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FormDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PostMapping("/{formId}")
    public ResponseEntity<FormDTO> create(@RequestBody @Valid FormDTO request, @PathVariable Long formId) throws NoParentException {
        Form entity = formAssembler.convertFromDTO(request);
        FormDTO response = formAssembler.convertToDTO(formService.create(entity, formId));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
