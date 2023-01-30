package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.Client;

public interface ClientService extends GenericService<Client> {
    
    public Client create(Client client, String username);
    
    public Client updateIfEnabled(Client client);
    
    public Client enable(Long id);
 
    public void deleteAllByIds(Iterable<? extends Long> ids);
    
}
