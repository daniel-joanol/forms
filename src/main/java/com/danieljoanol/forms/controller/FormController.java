package com.danieljoanol.forms.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danieljoanol.forms.assembler.FormAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.FormDTO;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;
import com.danieljoanol.forms.service.FormService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(Url.FORM)
@RequiredArgsConstructor
@SecurityRequirement(name = "{bearer.name}")
@PreAuthorize("hasRole('ROLE_USER')")
public class FormController {
    
    private final FormService formService;
    private final FormAssembler formAssembler;

    @Operation(summary = "Create", description = "Method to create a new form")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FormDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PostMapping("/{clientId}/")
    public ResponseEntity<FormDTO> create(@RequestBody @Valid FormDTO request, @PathVariable Long clientId) {
        String username = JwtTokenUtil.getUsername();
        Form entity = formAssembler.convertFromDTO(request);
        FormDTO response = formAssembler.convertToDTO(formService.create(entity, clientId, username));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
