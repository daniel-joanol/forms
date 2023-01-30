package com.danieljoanol.forms.service;

import java.util.List;

import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.User;

public interface ClientService extends GenericService<Client> {
    
    public Client create(Client client, String username);
    
    public Client updateIfEnabled(Client client);
    
    public Client enable(Long id);
 
    public void deleteAllByIds(Iterable<? extends Long> ids);

    public Client findByIdAndUsernames(Long id, List<String> usernames);

    public List<Client> findAllByUsernames(List<String> usernames);

    public List<Client> findAllByUsers(List<User> users);
    
}
