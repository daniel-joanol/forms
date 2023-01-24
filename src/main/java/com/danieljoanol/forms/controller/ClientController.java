package com.danieljoanol.forms.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danieljoanol.forms.assembler.ClientAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.ClientDTO;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.exception.NoParentException;
import com.danieljoanol.forms.repository.ClientRepository;
import com.danieljoanol.forms.service.ClientService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(Url.CLIENT)
@SecurityRequirement(name = "Bearer Authentication")
public class ClientController extends GenericController<Client, ClientDTO> {
    
    private final ClientService clientService;
    private final ClientAssembler clientAssembler;

    public ClientController(ClientRepository clientRepository, ClientAssembler clientAssembler, ClientService clientService) {
        super(clientRepository, clientAssembler);
        this.clientService = clientService;
        this.clientAssembler = clientAssembler;
    }

    @Operation(summary = "Create", description = "Method to create a new client")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClientDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PostMapping("/")
    public ResponseEntity<ClientDTO> create(@RequestBody @Valid ClientDTO request) throws NoParentException {
        Client entity = clientAssembler.convertFromDTO(request);
        ClientDTO response = clientAssembler.convertToDTO(clientService.create(entity));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
