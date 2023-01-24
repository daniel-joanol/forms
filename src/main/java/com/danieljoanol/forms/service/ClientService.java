package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.exception.NoParentException;

public interface ClientService extends GenericService<Client> {
    
    public Client create(Client client, Long shopId) throws NoParentException;
}
