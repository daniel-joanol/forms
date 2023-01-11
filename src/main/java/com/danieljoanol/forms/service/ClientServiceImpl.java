package com.danieljoanol.forms.service;

import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.repository.ClientRepository;

@Service
public class ClientServiceImpl extends GenericServiceImpl<Client> implements ClientService {
    
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        super(clientRepository);
        this.clientRepository = clientRepository;
    }
}
