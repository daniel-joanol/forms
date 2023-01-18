package com.danieljoanol.forms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danieljoanol.forms.assembler.ClientAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.ClientDTO;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.repository.ClientRepository;
import com.danieljoanol.forms.service.ClientService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(Url.CLIENT)
@SecurityRequirement(name = "Bearer Authentication")
public class ClientController extends GenericController<Client, ClientDTO> {
    
    @Autowired
    private ClientService clientService;

    public ClientController(ClientRepository clientRepository, ClientAssembler clientAssembler) {
        super(clientRepository, clientAssembler);
    }

}
