package com.danieljoanol.forms.assembler;

import org.springframework.stereotype.Component;

import com.danieljoanol.forms.dto.ClientDTO;
import com.danieljoanol.forms.entity.Client;

@Component
public class ClientAssembler extends GenericAssembler<Client, ClientDTO> {

    @Override
    public ClientDTO convertToDTO(Client entity) {
        return new ClientDTO(entity);
    }
    
}
